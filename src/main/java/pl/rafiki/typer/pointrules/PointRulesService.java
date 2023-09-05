package pl.rafiki.typer.pointrules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.rafiki.typer.pointrules.exceptions.PointRulesCodeAlreadyTakenException;
import pl.rafiki.typer.pointrules.exceptions.PointRulesDoesNotExistException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PointRulesService {

    private final PointRulesRepository pointRulesRepository;

    @Autowired
    public PointRulesService(PointRulesRepository pointRulesRepository) {
        this.pointRulesRepository = pointRulesRepository;
    }

    public List<PointRules> getAllPointRules() {
        return pointRulesRepository.findAll();
    }

    public PointRules getPointRules(Long pointRulesId) {
        Optional<PointRules> pointRulesOptional = pointRulesRepository.findById(pointRulesId);
        if (pointRulesOptional.isEmpty()) {
            throw new PointRulesDoesNotExistException("Point rules with id: " + pointRulesId + " does not exist!");
        }
        return pointRulesOptional.get();
    }

    public void addNewPointRules(PointRules pointRules) {
        boolean existsByCode = pointRulesRepository.existsByPointRulesCode(pointRules.getPointrulesCode());
        if (existsByCode) {
            throw new PointRulesCodeAlreadyTakenException("Point rules code already taken!");
        }
        pointRulesRepository.save(pointRules);
    }

    public void updatePointRules(Long pointRulesId, PointRules pointRules) {
        Optional<PointRules> pointRulesOptional = pointRulesRepository.findById(pointRulesId);
        if (pointRulesOptional.isEmpty()) {
            throw new PointRulesDoesNotExistException("Point rules with id: " + pointRulesId + " does not exist!");
        }

        PointRules existingPointRules = pointRulesOptional.get();

        String pointRulesCodeFromUpdate = pointRules.getPointrulesCode();
        if (pointRulesRepository.existsByPointRulesCode(pointRulesCodeFromUpdate)) {
            throw new PointRulesCodeAlreadyTakenException("Point rules code already taken!");
        } else if (pointRulesCodeFromUpdate != null && pointRulesCodeFromUpdate.length() > 0 && !Objects.equals(existingPointRules.getPointrulesCode(), pointRulesCodeFromUpdate)) {
            existingPointRules.setPointrulesCode(pointRulesCodeFromUpdate);
        }

        Integer scoreFromUpdate = pointRules.getScore();
        if (scoreFromUpdate != null && !Objects.equals(existingPointRules.getScore(), scoreFromUpdate)) {
            existingPointRules.setScore(scoreFromUpdate);
        }

        Integer winnerFromUpdate = pointRules.getWinner();
        if (winnerFromUpdate != null && !Objects.equals(existingPointRules.getWinner(), winnerFromUpdate)) {
            existingPointRules.setWinner(winnerFromUpdate);
        }

        pointRulesRepository.save(existingPointRules);
    }
}
