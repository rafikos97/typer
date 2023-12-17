package pl.rafiki.typer.bet;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.bet.exceptions.*;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchRepository;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.scoreboard.ScoreboardService;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;
import pl.rafiki.typer.utils.Result;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class BetService {
    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final ScoreboardService scoreboardService;

    @Autowired
    public BetService(BetRepository betRepository, UserRepository userRepository, MatchRepository matchRepository, ScoreboardService scoreboardService) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.scoreboardService = scoreboardService;
    }

    public void addNewBet(Long userId, Long matchId, BetDTO betDTO) {
        Bet bet = BetMapper.INSTANCE.betDtoToBet(betDTO, matchRepository, userRepository);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));
        bet.setUser(user);
    public void addNewBet(Long userId, Long matchId, BetDTO betDTO) {
        Bet bet = BetMapper.INSTANCE.betDtoToBet(betDTO, matchRepository, userRepository);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));
        bet.setUser(user);

        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));
        bet.setMatch(match);
        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));
        bet.setMatch(match);

        if (LocalDateTime.now().isAfter(match.getStartDateAndTime())) {
        if (LocalDateTime.now().isAfter(match.getStartDateAndTime())) {
            throw new CannotAddBetBecauseMatchAlreadyStartedException("You cannot place bet, because match already started!");
        }

        if (betRepository.existsByUserIdAndMatchId(userId, matchId)) {
            throw new BetAlreadyExistException("Bet already exist!");
        }

        betRepository.save(bet);
    }

    public List<BetDTO> getBets() {
        List<Bet> betList = betRepository.findAll();

        return mapToDTO(betList);
    }

    public List<BetDTO> getBetsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        List<Bet> betList = betRepository.findAllByUserId(userId);

        return mapToDTO(betList);
    }

    public List<BetDTO> getBetsByMatchId(Long matchId) {
        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        if (LocalDateTime.now().isBefore(match.getStartDateAndTime())) {
            throw new CannotDisplayBetsAsMatchHasNotStartedException("Cannot display bets as match has not started yet!");
        }

        List<Bet> betList = betRepository.findAllByMatchId(matchId);

        return mapToDTO(betList);
    }

    public BetDTO updateBet(Long betId, BetDTO betDTO) {
        Bet bet = BetMapper.INSTANCE.betDtoToBet(betDTO, matchRepository, userRepository);
        Bet existingBet = betRepository
                .findById(betId)
                .orElseThrow(() -> new BetDoesNotExistException("Bet with id: " + betId + " does not exist!"));

        if (LocalDateTime.now().isAfter(existingBet.getMatch().getStartDateAndTime())) {
            throw new CannotUpdateBetBecauseMatchAlreadyStartedException("You cannot update bet, because match already started!");
        }

        int firstTeamScoreFromUpdate = betDTO.getFirstTeamScore();
        if (!Objects.equals(firstTeamScoreFromUpdate, existingBet.getFirstTeamScore())) {
            existingBet.setFirstTeamScore(firstTeamScoreFromUpdate);
        }

        int secondTeamScoreFromUpdate = betDTO.getSecondTeamScore();
        if (!Objects.equals(secondTeamScoreFromUpdate, existingBet.getSecondTeamScore())) {
            existingBet.setSecondTeamScore(secondTeamScoreFromUpdate);
        }

        betRepository.save(existingBet);
        return BetMapper.INSTANCE.betToBetDto(existingBet);
    }

    @Transactional
    public void closeBetsAndCalculatePoints(Long matchId) {
        List<Bet> bets = betRepository.findAllByMatchId(matchId);

        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        if (!bets.isEmpty()) {
            for (Bet bet : bets) {
                bet.setFinished(true);
                Result result = calculateBetResults(match, bet);

                bet.setWinner(result.getResultForWinner());
                bet.setScore(result.getResultForScore());
                bet.setTotalPoints(result.getResultForWinner() + result.getResultForScore());

                scoreboardService.addScoreToScoreboard(bet.getUser().getId(), match.getTournament().getId(), result);
            }
        }
    }

    @Transactional
    public void recalculatePoints(Match match, Match updatedMatch) {
        if (match.getFirstTeamScore().equals(updatedMatch.getFirstTeamScore()) && match.getSecondTeamScore().equals(updatedMatch.getSecondTeamScore())) {
            return;
        }

        List<Bet> bets = betRepository.findAllByMatchId(match.getId());

        if (!bets.isEmpty()) {
            for (Bet bet : bets) {
                Result originalResult = calculateBetResults(match, bet);
                Result updatedResult = calculateBetResults(updatedMatch, bet);

                bet.setWinner(updatedResult.getResultForWinner());
                bet.setScore(updatedResult.getResultForScore());
                bet.setTotalPoints(updatedResult.getResultForWinner() + updatedResult.getResultForScore());

                scoreboardService.correctScoreInScoreboard(bet.getUser().getId(), updatedMatch.getTournament().getId(), originalResult, updatedResult);
            }
        }
    }

    private Result calculateBetResults(Match match, Bet bet) {
        int pointsForScore = match.getTournament().getPointRules().getScore();
        int pointsForWinner = match.getTournament().getPointRules().getWinner();
        int matchFirstTeamScore = match.getFirstTeamScore();
        int matchSecondTeamScore = match.getSecondTeamScore();
        boolean matchDraw = matchFirstTeamScore == matchSecondTeamScore;
        boolean matchFirstTeamWin = matchFirstTeamScore > matchSecondTeamScore;
        boolean matchSecondTeamWin = matchFirstTeamScore < matchSecondTeamScore;
        int betFirstTeamScore = bet.getFirstTeamScore();
        int betSecondTeamScore = bet.getSecondTeamScore();
        boolean betDraw = betFirstTeamScore == betSecondTeamScore;
        boolean betFirstTeamWin = betFirstTeamScore > betSecondTeamScore;
        boolean betSecondTeamWin = betFirstTeamScore < betSecondTeamScore;
        Result result = new Result();

        if ((matchDraw && betDraw) || (matchFirstTeamWin && betFirstTeamWin) || (matchSecondTeamWin && betSecondTeamWin)) {
            result.setResultForWinner(pointsForWinner);
        } else {
            result.setResultForWinner(0);
        }

        if (matchFirstTeamScore == betFirstTeamScore && matchSecondTeamScore == betSecondTeamScore) {
            result.setResultForScore(pointsForScore);
        } else {
            result.setResultForScore(0);
        }

        return result;
    }

    private static List<BetDTO> mapToDTO(List<Bet> betList) {
        return betList
                .stream()
                .map(BetMapper.INSTANCE::betToBetDto)
                .toList();
    }

    public void deleteBet(Long betId) {
        if (!betRepository.existsById(betId)) {
            throw new BetDoesNotExistException("Bet with id: " + betId + " does not exist!");
        }

        betRepository.deleteById(betId);
    }
}
