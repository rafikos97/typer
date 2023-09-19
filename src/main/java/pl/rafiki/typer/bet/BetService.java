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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public void addNewBet(Long userId, Long matchId, Bet bet) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }
        bet.setUser(userOptional.get());

        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isEmpty()) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }
        bet.setMatch(matchOptional.get());

        if (LocalDateTime.now().isAfter(matchOptional.get().getStartDateAndTime())) {
            throw new CannotAddBetBecauseMatchAlreadyStartedException("You cannot place bet, because match already started!");
        }

        if (betRepository.existsByUserIdAndMatchId(userId, matchId)) {
            throw new BetAlreadyExistException("Bet already exist!");
        }

        betRepository.save(bet);
    }

    public List<BetDTO> getBets() {
        List<Bet> betList = betRepository.findAll();
        List<BetDTO> betResponseList = new ArrayList<>();

        for (Bet bet : betList) {
            betResponseList.add(new BetDTO(bet));
        }

        return betResponseList;
    }


    public List<BetDTO> getBetsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        List<Bet> betList = betRepository.findAllByUserId(userId);
        List<BetDTO> betResponseList = new ArrayList<>();

        for (Bet bet : betList) {
            betResponseList.add(new BetDTO(bet));
        }

        return betResponseList;
    }

    public List<BetDTO> getBetsByMatchId(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }

        List<Bet> betList = betRepository.findAllByMatchId(matchId);
        List<BetDTO> betResponseList = new ArrayList<>();

        for (Bet bet : betList) {
            betResponseList.add(new BetDTO(bet));
        }

        return betResponseList;
    }

    public void updateBet(Long betId, Bet bet) {
        Optional<Bet> betOptional = betRepository.findById(betId);
        if (betOptional.isEmpty()) {
            throw new BetDoesNotExistException("Bet with id: " + betId + " does not exist!");
        }

        Bet existingBet = betOptional.get();

        if (LocalDateTime.now().isAfter(existingBet.getMatch().getStartDateAndTime())) {
            throw new CannotUpdateBetBecauseMatchAlreadyStartedException("You cannot update bet, because match already started!");
        }

        int firstTeamScoreFromUpdate = bet.getFirstTeamScore();
        if (!Objects.equals(firstTeamScoreFromUpdate, existingBet.getFirstTeamScore())) {
            existingBet.setFirstTeamScore(firstTeamScoreFromUpdate);
        }

        int secondTeamScoreFromUpdate = bet.getSecondTeamScore();
        if (!Objects.equals(secondTeamScoreFromUpdate, existingBet.getSecondTeamScore())) {
            existingBet.setSecondTeamScore(secondTeamScoreFromUpdate);
        }

        betRepository.save(existingBet);
    }

    @Transactional
    public void closeBets(Long matchId) {
        List<Bet> bets = betRepository.findAllByMatchId(matchId);

        Optional<Match> matchOptional = matchRepository.findById(matchId);
        if (matchOptional.isEmpty()) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }
        Match match = matchOptional.get();

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
}
