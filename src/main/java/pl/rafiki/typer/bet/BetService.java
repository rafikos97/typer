package pl.rafiki.typer.bet;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.bet.exceptions.BetAlreadyExistException;
import pl.rafiki.typer.bet.exceptions.BetDoesNotExistException;
import pl.rafiki.typer.bet.exceptions.CannotAddBetBecauseMatchAlreadyStartedException;
import pl.rafiki.typer.bet.exceptions.CannotUpdateBetBecauseMatchAlreadyStartedException;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchRepository;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.scoreboard.ScoreboardService;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;

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

        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));
        bet.setMatch(match);

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
        if (!matchRepository.existsById(matchId)) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }

        List<Bet> betList = betRepository.findAllByMatchId(matchId);

        return mapToDTO(betList);
    }

    public BetDTO updateBet(Long betId, BetDTO betDTO) {
<<<<<<< HEAD
<<<<<<< HEAD
=======
        Bet bet = BetMapper.INSTANCE.betDtoToBet(betDTO, matchRepository, userRepository);
>>>>>>> 34abc83 (Moved validation for null values from Bet class to DTO so it is possible to use DTO in POST and PUT methods.)
=======
        Bet bet = BetMapper.INSTANCE.betDtoToBet(betDTO, matchRepository, userRepository);
>>>>>>> 34abc83 (Moved validation for null values from Bet class to DTO so it is possible to use DTO in POST and PUT methods.)
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
    public void closeBets(Long matchId) {
        List<Bet> bets = betRepository.findAllByMatchId(matchId);

        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));

        Integer pointsForScore = match.getTournament().getPointRules().getScore();
        Integer pointsForWinner = match.getTournament().getPointRules().getWinner();

        int matchFirstTeamScore = match.getFirstTeamScore();
        int matchSecondTeamScore = match.getSecondTeamScore();
        boolean matchDraw = matchFirstTeamScore == matchSecondTeamScore;
        boolean matchFirstTeamWin = matchFirstTeamScore > matchSecondTeamScore;
        boolean matchSecondTeamWin = matchFirstTeamScore < matchSecondTeamScore;

        if (!bets.isEmpty()) {
            for (Bet bet : bets) {
                bet.setFinished(true);

                int betFirstTeamScore = bet.getFirstTeamScore();
                int betSecondTeamScore = bet.getSecondTeamScore();
                boolean betDraw = betFirstTeamScore == betSecondTeamScore;
                boolean betFirstTeamWin = betFirstTeamScore > betSecondTeamScore;
                boolean betSecondTeamWin = betFirstTeamScore < betSecondTeamScore;

                if (matchDraw && betDraw) {
                    bet.setWinner(pointsForWinner);
                } else if ((matchFirstTeamWin && betFirstTeamWin) || (matchSecondTeamWin && betSecondTeamWin)) {
                    bet.setWinner(pointsForWinner);
                } else {
                    bet.setWinner(0);
                }

                if (matchFirstTeamScore == betFirstTeamScore && matchSecondTeamScore == betSecondTeamScore) {
                    bet.setScore(pointsForScore);
                } else {
                    bet.setScore(0);
                }

                bet.setTotalPoints(bet.getWinner() + bet.getScore());

                scoreboardService.addScore(bet.getUser().getId(), match.getTournament().getId(), bet.getScore(), bet.getWinner());
            }
        }
    }

    private static List<BetDTO> mapToDTO(List<Bet> betList) {
        return betList
                .stream()
                .map(BetMapper.INSTANCE::betToBetDto)
                .toList();
    }
}
