package pl.rafiki.typer.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/typer/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping(path = "/all")
    public List<Match> getMatches() {
        return matchService.getMatches();
    }

    @GetMapping(path = "/{matchId}")
    public Match getMatch(@PathVariable(name = "matchId") Long matchId) {
        return matchService.getMatch(matchId);
    }

    @PostMapping(path = "/add")
    public void addNewMatch(@RequestBody Match match) {
        matchService.addNewMatch(match);
    }

    @PutMapping(path = "/{matchId}")
    public void updateMatch(@PathVariable(name = "matchId") Long matchId, @RequestBody @Valid Match match) {
        matchService.putUpdateMatch(matchId, match);
    }
}
