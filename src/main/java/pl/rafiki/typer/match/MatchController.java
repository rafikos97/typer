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

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/all")
    public List<MatchDTO> getMatches() {
        return matchService.getMatches();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(path = "/{matchId}")
    public MatchDTO getMatch(@PathVariable(name = "matchId") Long matchId) {
        return matchService.getMatch(matchId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public void addNewMatch(@RequestBody @Valid MatchDTO matchDTO) {
        matchService.addNewMatch(matchDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{matchId}")
    public MatchDTO updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody @Valid MatchDTO matchDTO) {
        return matchService.putUpdateMatch(matchId, matchDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{matchId}")
    public MatchDTO patchUpdateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody MatchDTO matchDTO) {
        return matchService.patchUpdateMatch(matchId, matchDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/{matchId}/finish")
    public void finishMatch(@PathVariable(name = "matchId") Long matchId) {
        matchService.finishMatch(matchId);
    }
}
