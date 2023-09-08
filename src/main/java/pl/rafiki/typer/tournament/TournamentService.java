package pl.rafiki.typer.tournament;

import org.springframework.stereotype.Service;
import pl.rafiki.typer.pointrules.PointRules;
import pl.rafiki.typer.pointrules.PointRulesRepository;
import pl.rafiki.typer.pointrules.exceptions.PointRulesDoesNotExistException;
import pl.rafiki.typer.tournament.exceptions.TournamentCodeAlreadyTakenException;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final PointRulesRepository pointRulesRepository;

    public TournamentService(TournamentRepository tournamentRepository, PointRulesRepository pointRulesRepository) {
        this.tournamentRepository = tournamentRepository;
        this.pointRulesRepository = pointRulesRepository;
    }

    public List<Tournament> getTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament getTournament(Long tournamentId) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        if (tournamentOptional.isEmpty()) {
            throw new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!");
        }

        return tournamentOptional.get();
    }


    public void addNewTournament(Tournament tournament) {
        boolean existsByCode = tournamentRepository.existsByTournamentCode(tournament.getTournamentCode());
        if (existsByCode) {
            throw new TournamentCodeAlreadyTakenException("Tournament code already taken!");
        }

        String pointRulesCode = tournament.getPointRulesCode();
        Optional<PointRules> pointRulesByCodeOptional = pointRulesRepository.findPointRulesByPointRulesCode(pointRulesCode);

        if (pointRulesByCodeOptional.isEmpty()) {
            throw new PointRulesDoesNotExistException("Point rules with code " + pointRulesCode + " does not exist!");
        }
        tournament.setPointRules(pointRulesByCodeOptional.get());

        tournamentRepository.save(tournament);
    }

    public void updateTournament(Long tournamentId, Tournament tournament) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        if (tournamentOptional.isEmpty()) {
            throw new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!");
        }

        Tournament existingTournament = tournamentOptional.get();

        String tournamentNameFromUpdate = tournament.getTournamentName();
        if (tournamentNameFromUpdate != null && tournamentNameFromUpdate.length() > 0 && !Objects.equals(existingTournament.getTournamentName(), tournamentNameFromUpdate)) {
            existingTournament.setTournamentName(tournamentNameFromUpdate);
        }

        String tournamentCodeFromUpdate = tournament.getTournamentCode();
        if (tournamentRepository.existsByTournamentCode(tournamentCodeFromUpdate)) {
            throw new TournamentCodeAlreadyTakenException("Tournament code: " + tournamentCodeFromUpdate + " already taken!");
        } else if (tournamentCodeFromUpdate != null && tournamentCodeFromUpdate.length() > 0 && !Objects.equals(existingTournament.getTournamentCode(), tournamentCodeFromUpdate)) {
            existingTournament.setTournamentCode(tournamentCodeFromUpdate);
        }

        boolean statusFromUpdate = tournament.isFinished();
        if (!Objects.equals(existingTournament.isFinished(), statusFromUpdate)) {
            existingTournament.setFinished(statusFromUpdate);
        }

        String pointRulesCodeFromUpdate = tournament.getPointRulesCode();
        Optional<PointRules> pointRulesFromUpdateOpt = pointRulesRepository.findPointRulesByPointRulesCode(pointRulesCodeFromUpdate);
        if (pointRulesFromUpdateOpt.isEmpty()) {
            throw new PointRulesDoesNotExistException("Point rules with code: " + pointRulesCodeFromUpdate + " does not exist!");
        } else if (!Objects.equals(existingTournament.getPointRules(), pointRulesFromUpdateOpt.get())) {
            existingTournament.setPointRules(pointRulesFromUpdateOpt.get());
            existingTournament.setPointRulesCode(pointRulesCodeFromUpdate);
        }

        tournamentRepository.save(existingTournament);
    }
}