package pl.rafiki.typer.scoreboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/typer/scoreboard")
public class ScoreboardController {

    private final ScoreboardService scoreboardService;

    @Autowired
    public ScoreboardController(ScoreboardService scoreboardService) {
        this.scoreboardService = scoreboardService;
    }

    @GetMapping(path = "/user/{tournamentId}")
    public List<Scoreboard> getScoreboard(@PathVariable(name = "tournamentId") Long tournamentId) {
        return scoreboardService.getScoreboard(tournamentId);
    }
}
