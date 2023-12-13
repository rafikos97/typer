package pl.rafiki.typer.tournament;

import org.springframework.stereotype.Service;
import pl.rafiki.typer.pointrules.PointRules;
import pl.rafiki.typer.pointrules.PointRulesRepository;
import pl.rafiki.typer.pointrules.exceptions.PointRulesDoesNotExistException;
import pl.rafiki.typer.tournament.exceptions.TournamentCodeAlreadyTakenException;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

import java.util.List;
import java.util.Objects;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final PointRulesRepository pointRulesRepository;

    public TournamentService(TournamentRepository tournamentRepository, PointRulesRepository pointRulesRepository) {
        this.tournamentRepository = tournamentRepository;
        this.pointRulesRepository = pointRulesRepository;
    }

    public List<TournamentDTO> getTournaments() {
        List<Tournament> tournamentList = tournamentRepository.findAll();

        return tournamentList
                .stream()
                .map(TournamentMapper.INSTANCE::tournamentToTournamentDto)
                .toList();
    }

    public TournamentDTO getTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!"));

        return TournamentMapper.INSTANCE.tournamentToTournamentDto(tournament);
    }


    public void addNewTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = TournamentMapper.INSTANCE.tournamentDtoToTournament(tournamentDTO, pointRulesRepository);
        boolean existsByCode = tournamentRepository.existsByTournamentCode(tournament.getTournamentCode());
        if (existsByCode) {
            throw new TournamentCodeAlreadyTakenException("Tournament code already taken!");
        }

        String pointRulesCode = tournament.getPointRules().getPointRulesCode();

        PointRules pointRules = pointRulesRepository
                .findPointRulesByPointRulesCode(pointRulesCode)
                .orElseThrow(() -> new PointRulesDoesNotExistException("Point rules with code " + pointRulesCode + " does not exist!"));

        tournament.setPointRules(pointRules);

        tournamentRepository.save(tournament);
    }

    public TournamentDTO updateTournament(Long tournamentId, TournamentDTO tournamentDTO) {
        Tournament existingTournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() -> new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!"));

        String tournamentNameFromUpdate = tournamentDTO.getTournamentName();
        if (tournamentNameFromUpdate != null && !tournamentNameFromUpdate.isEmpty() && !Objects.equals(existingTournament.getTournamentName(), tournamentNameFromUpdate)) {
            existingTournament.setTournamentName(tournamentNameFromUpdate);
        }

        String tournamentCodeFromUpdate = tournamentDTO.getTournamentCode();

        if (tournamentCodeFromUpdate != null && !tournamentCodeFromUpdate.isEmpty() && !Objects.equals(existingTournament.getTournamentCode(), tournamentCodeFromUpdate)) {
            if (tournamentRepository.existsByTournamentCode(tournamentCodeFromUpdate)) {
                throw new TournamentCodeAlreadyTakenException("Tournament code: " + tournamentCodeFromUpdate + " already taken!");
            } else {
                existingTournament.setTournamentCode(tournamentCodeFromUpdate);
            }
        }

        String pointRulesCodeFromUpdate = tournamentDTO.getPointRulesCode();

        if (pointRulesCodeFromUpdate != null && !pointRulesCodeFromUpdate.isEmpty() && !Objects.equals(existingTournament.getPointRules().getPointRulesCode(), pointRulesCodeFromUpdate)) {
            PointRules pointRules = pointRulesRepository
                    .findPointRulesByPointRulesCode(pointRulesCodeFromUpdate)
                    .orElseThrow(() -> new PointRulesDoesNotExistException("Point rules with code: " + pointRulesCodeFromUpdate + " does not exist!"));
            existingTournament.setPointRules(pointRules);
        }

        tournamentRepository.save(existingTournament);
        return TournamentMapper.INSTANCE.tournamentToTournamentDto(existingTournament);
    }

    public void deleteTournament(Long tournamentId) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new TournamentDoesNotExistException("Tournament with id: " + tournamentId + " does not exist!");
        }

        tournamentRepository.deleteById(tournamentId);
    }
}
