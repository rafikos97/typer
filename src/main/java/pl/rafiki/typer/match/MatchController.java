package pl.rafiki.typer.match;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/match")
@Validated
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<Match> getMatches() {
        return matchService.getMatches();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{matchId}")
    public Match getMatch(@PathVariable(name = "matchId") Long matchId) {
        return matchService.getMatch(matchId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public void addNewMatch(@RequestBody @Valid Match match) {
        matchService.addNewMatch(match);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{matchId}")
    public void updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody @Valid Match match) {
        matchService.putUpdateMatch(matchId, match);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{matchId}")
    public void updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody MatchDTO dto) {
        matchService.patchUpdateMatch(matchId, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/{matchId}/finish")
    public void finishMatch(@PathVariable(name = "matchId") Long matchId) {
        matchService.finishMatch(matchId);
    }
}
