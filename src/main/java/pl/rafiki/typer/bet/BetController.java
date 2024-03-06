package pl.rafiki.typer.bet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addNewBet(@PathVariable(name = "userId") Long userId, @PathVariable("matchId") Long matchId, @RequestBody @Valid BetDTO betDTO) {
        betService.addNewBet(userId, matchId, betDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all bets.",
            description = "Method to get all existing bets."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public ResponseEntity<List<BetDTO>> getBets() {
        List<BetDTO> betList = betService.getBets();
        return new ResponseEntity<>(betList, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all bets by userId.",
            description = "Method to get all bets associated with specific user."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{userId}")
    public ResponseEntity<List<BetDTO>> getUserBets(@PathVariable(name = "userId") Long userId) {
        List<BetDTO> betList = betService.getBetsByUserId(userId);
        return new ResponseEntity<>(betList, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all bets by matchId.",
            description = "Method to get all bets associated with specific match."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/match/{matchId}")
    public ResponseEntity<List<BetDTO>> getBetsByMatchId(@PathVariable(name = "matchId") Long matchId) {
        List<BetDTO> betList = betService.getBetsByMatchId(matchId);
        return new ResponseEntity<>(betList, HttpStatus.OK);
    }

    @Operation(
            summary = "Update bet by betId.",
            description = "Method to update specific bet."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(path = "/{betId}")
    public ResponseEntity<BetDTO> updateBet(@PathVariable(name = "betId") Long betId, @RequestBody @Valid BetDTO betDTO) {
        BetDTO bet = betService.updateBet(betId, betDTO);
        return new ResponseEntity<>(bet, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete bet.",
            description = "Method to delete specific bet by betId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{betId}")
    public ResponseEntity<?> deleteBet(@PathVariable("betId") Long betId) {
        betService.deleteBet(betId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
