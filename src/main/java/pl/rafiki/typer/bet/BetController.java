package pl.rafiki.typer.bet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/bet")
public class BetController {

    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping(path = "/user/{userId}/{matchId}")
    public void addNewBet(@PathVariable(name = "userId") Long userId, @PathVariable("matchId") Long matchId, @RequestBody Bet bet) {
        betService.addNewBet(userId, matchId, bet);
    }

    @GetMapping(path = "/admin/all")
    public List<Bet> getBets() {
        return betService.getBets();
    }

    @GetMapping(path = "/user/{userId}")
    public List<Bet> getUserBets(@PathVariable(name = "userId") Long userId) {
        return betService.getBetsByUserId(userId);
    }

    @GetMapping(path = "/admin/match/{matchId}")
    public List<Bet> getBetsByMatchId(@PathVariable(name = "matchId") Long matchId) {
        return betService.getBetsByMatchId(matchId);
    }

    @PutMapping(path = "/user/{betId}")
    public void updateBet(@PathVariable(name = "betId") Long betId, @RequestBody Bet bet) {
        betService.updateBet(betId, bet);
    }
}
