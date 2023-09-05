package pl.rafiki.typer.bet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.bet.exceptions.BetAlreadyExistException;
import pl.rafiki.typer.bet.exceptions.BetDoesNotExistException;
import pl.rafiki.typer.bet.exceptions.CannotAddBetBecauseMatchAlreadyStartedException;
import pl.rafiki.typer.bet.exceptions.CannotUpdateBetBecauseMatchAlreadyStartedException;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchRepository;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public BetService(BetRepository betRepository, UserRepository userRepository, MatchRepository matchRepository) {
        this.betRepository = betRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
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

    public List<Bet> getBets() {
        return betRepository.findAll();
    }


    public List<Bet> getBetsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserDoesNotExistException("User with id: " + userId + " does not exist!");
        }

        return betRepository.findAllByUserId(userId);
    }

    public List<Bet> getBetsByMatchId(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!");
        }

        return betRepository.findAllByMatchId(matchId);
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
}
