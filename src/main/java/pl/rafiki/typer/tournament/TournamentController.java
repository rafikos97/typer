package pl.rafiki.typer.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping(path = "/all")
    public List<Tournament> getTournaments() {
        return tournamentService.getTournaments();
    }

    @GetMapping(path = "/{tournamentId}")
    public Tournament getTournament(@PathVariable(name = "tournamentId") Long tournamentId) {
        return tournamentService.getTournament(tournamentId);
    }

    @PostMapping(path = "/add")
    public void addNewTournament(@RequestBody Tournament tournament) {
        tournamentService.addNewTournament(tournament);
    }

    @PutMapping(path = "/{tournamentId}")
    public void updateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody Tournament tournament) {
        tournamentService.updateTournament(tournamentId, tournament);
    }
}
