package pl.rafiki.typer.tournament;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/tournament")
@Validated
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping(path = "/admin/all")
    public List<Tournament> getTournaments() {
        return tournamentService.getTournaments();
    }

    @GetMapping(path = "/admin/{tournamentId}")
    public Tournament getTournament(@PathVariable(name = "tournamentId") Long tournamentId) {
        return tournamentService.getTournament(tournamentId);
    }

    @PostMapping(path = "/admin/add")
    public void addNewTournament(@RequestBody @Valid Tournament tournament) {
        tournamentService.addNewTournament(tournament);
    }

    @PutMapping(path = "admin/{tournamentId}")
    public void updateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody @Valid Tournament tournament) {
        tournamentService.putUpdateTournament(tournamentId, tournament);
    }

    @PatchMapping(path = "admin/{tournamentId}")
    public void updateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody TournamentDTO dto) {
        tournamentService.patchUpdateTournament(tournamentId, dto);
    }
}
