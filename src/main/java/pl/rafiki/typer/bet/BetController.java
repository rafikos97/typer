package pl.rafiki.typer.bet;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/bet")
@Validated
public class BetController {
    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(path = "/{userId}/{matchId}")
    public void addNewBet(@PathVariable(name = "userId") Long userId, @PathVariable("matchId") Long matchId, @RequestBody @Valid BetDTO betDTO) {
        betService.addNewBet(userId, matchId, betDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<BetDTO> getBets() {
        return betService.getBets();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{userId}")
    public List<BetDTO> getUserBets(@PathVariable(name = "userId") Long userId) {
        return betService.getBetsByUserId(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/match/{matchId}")
    public List<BetDTO> getBetsByMatchId(@PathVariable(name = "matchId") Long matchId) {
        return betService.getBetsByMatchId(matchId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(path = "/{betId}")
    public BetDTO updateBet(@PathVariable(name = "betId") Long betId, @RequestBody @Valid BetDTO betDTO) {
        return betService.updateBet(betId, betDTO);
    }
}
