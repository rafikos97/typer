package pl.rafiki.typer.tournament;

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
@RequestMapping(path = "/typer/tournament")
@Validated
@Tag(name = "Tournament", description = "Tournament management APIs")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @Operation(
            summary = "Get all tournaments.",
            description = "Method to get all existing tournaments."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/all")
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        List<TournamentDTO> tournamentList = tournamentService.getTournaments();
        return new ResponseEntity<>(tournamentList, HttpStatus.OK);
    }

    @Operation(
            summary = "Get tournament by tournamentId.",
            description = "Method to get all existing matches."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{tournamentId}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable(name = "tournamentId") Long tournamentId) {
        TournamentDTO tournament = tournamentService.getTournament(tournamentId);
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    @Operation(
            summary = "Add new tournament.",
            description = "Method for adding new tournament."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewTournament(@RequestBody @Valid TournamentDTO tournamentDTO) {
        tournamentService.addNewTournament(tournamentDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update tournament by tournamentId.",
            description = "Method to update specific tournament."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{tournamentId}")
    public ResponseEntity<TournamentDTO> updateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody @Valid TournamentDTO tournamentDTO) {
        TournamentDTO tournament = tournamentService.updateTournament(tournamentId, tournamentDTO);
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    @Operation(
            summary = "Update tournament by tournamentId.",
            description = "Method to partially update specific tournament. All fields are optional here."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{tournamentId}")
    public ResponseEntity<TournamentDTO> patchUpdateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody TournamentDTO tournamentDTO) {
        TournamentDTO tournament = tournamentService.updateTournament(tournamentId, tournamentDTO);
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete tournament.",
            description = "Method to delete specific tournament by tournamentId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{tournamentId}")
    public ResponseEntity<?> deleteTournament(@PathVariable("tournamentId") Long tournamentId) {
        tournamentService.deleteTournament(tournamentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
