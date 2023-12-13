package pl.rafiki.typer.tournament;

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
@RequestMapping(path = "/typer/tournament")
@Validated
@Tag(name = "Tournament", description = "Tournament management APIs")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
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
    public List<TournamentDTO> getTournaments() {
        return tournamentService.getTournaments();
    }

    @Operation(
            summary = "Get tournament by tournamentId.",
            description = "Method to get all existing matches."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{tournamentId}")
    public TournamentDTO getTournament(@PathVariable(name = "tournamentId") Long tournamentId) {
        return tournamentService.getTournament(tournamentId);
    }

    @Operation(
            summary = "Add new tournament.",
            description = "Method for adding new tournament."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public void addNewTournament(@RequestBody @Valid TournamentDTO tournamentDTO) {
        tournamentService.addNewTournament(tournamentDTO);
    }

    @Operation(
            summary = "Update tournament by tournamentId.",
            description = "Method to update specific tournament."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{tournamentId}")
    public TournamentDTO updateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody @Valid TournamentDTO tournamentDTO) {
        return tournamentService.updateTournament(tournamentId, tournamentDTO);
    }

    @Operation(
            summary = "Update tournament by tournamentId.",
            description = "Method to partially update specific tournament. All fields are optional here."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{tournamentId}")
    public TournamentDTO patchUpdateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody TournamentDTO tournamentDTO) {
        return tournamentService.updateTournament(tournamentId, tournamentDTO);
    }

    @Operation(
            summary = "Delete tournament.",
            description = "Method to delete specific tournament by tournamentId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{tournamentId}")
    public void deleteTournament(@PathVariable("tournamentId") Long tournamentId) {
        tournamentService.deleteTournament(tournamentId);
    }
}
