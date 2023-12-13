package pl.rafiki.typer.match;

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
@RequestMapping(path = "/typer/match")
@Validated
@Tag(name = "Match", description = "Match management APIs")
public class MatchController {

    private final MatchService matchService;

    @Autowired
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
    public List<MatchDTO> getMatches() {
        return matchService.getMatches();
    }

    @Operation(
            summary = "Get match by matchId.",
            description = "Method to get specific match by matchId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{matchId}")
    public MatchDTO getMatch(@PathVariable(name = "matchId") Long matchId) {
        return matchService.getMatch(matchId);
    }

    @Operation(
            summary = "Add new match.",
            description = "Method for adding new match."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public void addNewMatch(@RequestBody @Valid AddMatchDTO addMatchDTO) {
        matchService.addNewMatch(addMatchDTO);
    }

    @Operation(
            summary = "Update match by matchId.",
            description = "Method to update specific match. For finished match, firstTeamScore and secondTeamScore fields are mandatory."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{matchId}")
    public MatchDTO updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody @Valid UpdateMatchDTO updateMatchDTO) {
        return matchService.putUpdateMatch(matchId, updateMatchDTO);
    }

    @Operation(
            summary = "Update match by matchId.",
            description = "Method to partially update specific match. All fields are optional here."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{matchId}")
    public MatchDTO patchUpdateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody UpdateMatchDTO updateMatchDTO) {
        return matchService.patchUpdateMatch(matchId, updateMatchDTO);
    }

    @Operation(
            summary = "Finish match.",
            description = "Method to finish specific match."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/{matchId}/finish")
    public void finishMatch(@PathVariable(name = "matchId") Long matchId, @Valid @RequestBody FinishMatchDTO finishMatchDTO) {
        matchService.finishMatch(matchId, finishMatchDTO);
    }

    @Operation(
            summary = "Delete match.",
            description = "Method to delete specific match by matchId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{matchId}")
    public void deleteMatch(@PathVariable("matchId") Long matchId) {
        matchService.deleteMatch(matchId);
    }
}
