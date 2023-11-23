package pl.rafiki.typer.tournament;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<TournamentDTO> getTournaments() {
        return tournamentService.getTournaments();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{tournamentId}")
    public TournamentDTO getTournament(@PathVariable(name = "tournamentId") Long tournamentId) {
        return tournamentService.getTournament(tournamentId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public void addNewTournament(@RequestBody @Valid Tournament tournament) {
        tournamentService.addNewTournament(tournament);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{tournamentId}")
    public TournamentDTO updateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody @Valid Tournament tournament) {
        return tournamentService.putUpdateTournament(tournamentId, tournament);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{tournamentId}")
    public TournamentDTO updateTournament(@PathVariable(name = "tournamentId") Long tournamentId, @RequestBody TournamentDTO dto) {
        return tournamentService.patchUpdateTournament(tournamentId, dto);
    }
}
