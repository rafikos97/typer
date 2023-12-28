package pl.rafiki.typer.pointrules;

import org.springframework.stereotype.Service;
import pl.rafiki.typer.pointrules.exceptions.PointRulesCodeAlreadyTakenException;
import pl.rafiki.typer.pointrules.exceptions.PointRulesDoesNotExistException;

import java.util.List;
import java.util.Objects;

@Service
public class PointRulesService {
    private final PointRulesRepository pointRulesRepository;

    public PointRulesService(PointRulesRepository pointRulesRepository) {
        this.pointRulesRepository = pointRulesRepository;
    }

    public List<PointRulesDTO> getAllPointRules() {
        List<PointRules> pointRulesList = pointRulesRepository.findAll();

        return pointRulesList
                .stream()
                .map(PointRulesMapper.INSTANCE::mapPointRulesToPointRulesDto)
                .toList();
    }

    public PointRulesDTO getPointRules(Long pointRulesId) {
        PointRules pointRules = pointRulesRepository
                .findById(pointRulesId)
                .orElseThrow(() -> new PointRulesDoesNotExistException("Point rules with id: " + pointRulesId + " does not exist!"));

        return PointRulesMapper.INSTANCE.mapPointRulesToPointRulesDto(pointRules);
    }

    public void addNewPointRules(PointRulesDTO pointRulesDTO) {
        PointRules pointRules = PointRulesMapper.INSTANCE.mapPointRulesDtoToPointRules(pointRulesDTO);
        boolean existsByCode = pointRulesRepository.existsByPointRulesCode(pointRulesDTO.getPointRulesCode());
        if (existsByCode) {
            throw new PointRulesCodeAlreadyTakenException("Point rules code already taken!");
        }

        pointRulesRepository.save(pointRules);
    }

    public PointRulesDTO updatePointRules(Long pointRulesId, PointRulesDTO pointRulesDTO) {
        PointRules existingPointRules = pointRulesRepository
                .findById(pointRulesId)
                .orElseThrow(() -> new PointRulesDoesNotExistException("Point rules with id: " + pointRulesId + " does not exist!"));

        String pointRulesCodeFromUpdate = pointRulesDTO.getPointRulesCode();

        if (pointRulesCodeFromUpdate != null && !pointRulesCodeFromUpdate.isEmpty() && !Objects.equals(existingPointRules.getPointRulesCode(), pointRulesCodeFromUpdate)) {
            if (pointRulesRepository.existsByPointRulesCode(pointRulesCodeFromUpdate)) {
                throw new PointRulesCodeAlreadyTakenException("Point rules code already taken!");
            } else {
                existingPointRules.setPointRulesCode(pointRulesCodeFromUpdate);
            }
        }

        Integer scoreFromUpdate = pointRulesDTO.getScore();
        if (scoreFromUpdate != null && !Objects.equals(existingPointRules.getScore(), scoreFromUpdate)) {
            existingPointRules.setScore(scoreFromUpdate);
        }

        Integer winnerFromUpdate = pointRulesDTO.getWinner();
        if (winnerFromUpdate != null && !Objects.equals(existingPointRules.getWinner(), winnerFromUpdate)) {
            existingPointRules.setWinner(winnerFromUpdate);
        }

        pointRulesRepository.save(existingPointRules);
        return PointRulesMapper.INSTANCE.mapPointRulesToPointRulesDto(existingPointRules);
    }

    public void deletePointRules(Long pointrulesId) {
        if (!pointRulesRepository.existsById(pointrulesId)) {
            throw new PointRulesDoesNotExistException("Point rules with id: " + pointrulesId + " does not exist!");
        }

        pointRulesRepository.deleteById(pointrulesId);
    }
}