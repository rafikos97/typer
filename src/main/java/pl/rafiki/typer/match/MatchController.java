package pl.rafiki.typer.match;

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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/typer/match")
@Validated
@Tag(name = "Match", description = "Match management APIs")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @Operation(
            summary = "Get all matches.",
            description = "Method to get all existing matches."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/all")
    public ResponseEntity<List<MatchDTO>> getMatches (
            @RequestParam(required = false) Boolean finished,
            @RequestParam(required = false) LocalDate startDateFrom,
            @RequestParam(required = false) LocalDate startDateTo) {
        List<MatchDTO> matchList = matchService.getMatches(finished, startDateFrom, startDateTo);
        return new ResponseEntity<>(matchList, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all matches for specific tournament.",
            description = "Method to get matches by tournamentId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/tournament/{tournamentId}")
    public ResponseEntity<List<MatchDTO>> getMatchesByTournamentId (
            @PathVariable Long tournamentId,
            @RequestParam(required = false) Boolean finished,
            @RequestParam(required = false) LocalDate startDateFrom,
            @RequestParam(required = false) LocalDate startDateTo) {
        List<MatchDTO> matchList = matchService.getMatchesByTournamentId(tournamentId, finished, startDateFrom, startDateTo);
        return new ResponseEntity<>(matchList, HttpStatus.OK);
    }

    @Operation(
            summary = "Get match by matchId.",
            description = "Method to get specific match by matchId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{matchId}")
    public ResponseEntity<MatchDTO> getMatch(@PathVariable(name = "matchId") Long matchId) {
        MatchDTO match = matchService.getMatch(matchId);
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    @Operation(
            summary = "Add new match.",
            description = "Method for adding new match."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewMatch(@RequestBody @Valid AddMatchDTO addMatchDTO) {
        matchService.addNewMatch(addMatchDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update match by matchId.",
            description = "Method to update specific match. For finished match, firstTeamScore and secondTeamScore fields are mandatory."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{matchId}")
    public ResponseEntity<MatchDTO> updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody @Valid UpdateMatchDTO updateMatchDTO) {
        MatchDTO match = matchService.putUpdateMatch(matchId, updateMatchDTO);
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    @Operation(
            summary = "Update match by matchId.",
            description = "Method to partially update specific match. All fields are optional here."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{matchId}")
    public ResponseEntity<MatchDTO> patchUpdateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody UpdateMatchDTO updateMatchDTO) {
        MatchDTO match = matchService.patchUpdateMatch(matchId, updateMatchDTO);
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    @Operation(
            summary = "Finish match.",
            description = "Method to finish specific match."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/{matchId}/finish")
    public ResponseEntity<?> finishMatch(@PathVariable(name = "matchId") Long matchId, @Valid @RequestBody FinishMatchDTO finishMatchDTO) {
        matchService.finishMatch(matchId, finishMatchDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Delete match.",
            description = "Method to delete specific match by matchId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{matchId}")
    public ResponseEntity<?> deleteMatch(@PathVariable("matchId") Long matchId) {
        matchService.deleteMatch(matchId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
