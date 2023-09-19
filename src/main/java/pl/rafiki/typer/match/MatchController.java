package pl.rafiki.typer.match;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(path = "/admin/all")
    public List<Match> getMatches() {
        return matchService.getMatches();
    }

    @GetMapping(path = "/admin/{matchId}")
    public Match getMatch(@PathVariable(name = "matchId") Long matchId) {
        return matchService.getMatch(matchId);
    }

    @PostMapping(path = "/admin/add")
    public void addNewMatch(@RequestBody @Valid Match match) {
        matchService.addNewMatch(match);
    }

    @PutMapping(path = "/admin/{matchId}")
    public void updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody @Valid Match match) {
        matchService.putUpdateMatch(matchId, match);
    }

    @PatchMapping(path = "/admin/{matchId}")
    public void updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody MatchDTO dto) {
        matchService.patchUpdateMatch(matchId, dto);
    }

    @PostMapping(path = "/admin/{matchId}/finish")
    public void finishMatch(@PathVariable(name = "matchId") Long matchId) {
        matchService.finishMatch(matchId);
    }
}
