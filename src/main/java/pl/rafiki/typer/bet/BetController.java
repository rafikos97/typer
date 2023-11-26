package pl.rafiki.typer.bet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/bet")
@Validated
@Tag(name = "Bet", description = "Bet management APIs")
public class BetController {
    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @Operation(
            summary = "Add bet by userId and match Id.",
            description = "Method for adding new bet for a specific user and match."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(path = "/{userId}/{matchId}")
    public void addNewBet(@PathVariable(name = "userId") Long userId, @PathVariable("matchId") Long matchId, @RequestBody @Valid BetDTO betDTO) {
        betService.addNewBet(userId, matchId, betDTO);
    public void addNewBet(@PathVariable(name = "userId") Long userId, @PathVariable("matchId") Long matchId, @RequestBody @Valid BetDTO betDTO) {
        betService.addNewBet(userId, matchId, betDTO);
    }

    @Operation(
            summary = "Get all bets.",
            description = "Method to get all existing bets."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<BetDTO> getBets() {
        return betService.getBets();
    }

    @Operation(
            summary = "Get all bets by userId.",
            description = "Method to get all bets associated with specific user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{userId}")
    public List<BetDTO> getUserBets(@PathVariable(name = "userId") Long userId) {
        return betService.getBetsByUserId(userId);
    }

    @Operation(
            summary = "Get all bets by matchId.",
            description = "Method to get all bets associated with specific match."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/match/{matchId}")
    public List<BetDTO> getBetsByMatchId(@PathVariable(name = "matchId") Long matchId) {
        return betService.getBetsByMatchId(matchId);
    }

    @Operation(
            summary = "Update bet by betId.",
            description = "Method to update specific bet."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(path = "/{betId}")
    public BetDTO updateBet(@PathVariable(name = "betId") Long betId, @RequestBody @Valid BetDTO betDTO) {
        return betService.updateBet(betId, betDTO);
    }
}
