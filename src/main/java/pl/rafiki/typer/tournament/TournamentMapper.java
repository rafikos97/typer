package pl.rafiki.typer.tournament;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pl.rafiki.typer.pointrules.PointRules;
import pl.rafiki.typer.pointrules.PointRulesRepository;
import pl.rafiki.typer.pointrules.exceptions.PointRulesDoesNotExistException;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TournamentMapper {
    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

    @Mapping(source = "pointRules", target = "pointRulesCode", qualifiedByName = "pointRulesToPointRulesCode")
    TournamentDTO tournamentToTournamentDto(Tournament tournament);

    @Mapping(source = "pointRulesCode", target = "pointRules", qualifiedByName = "pointRulesCodeToPointRules")
    Tournament tournamentDtoToTournament(TournamentDTO tournamentDTO, @Context PointRulesRepository pointRulesRepository);

    @Named("pointRulesToPointRulesCode")
    default String pointRulesToPointRulesCode(PointRules pointRules) {
        return pointRules.getPointRulesCode();
    }

    @Named("pointRulesCodeToPointRules")
    default PointRules pointRulesCodeToPointRules(String pointRulesCode, @Context PointRulesRepository pointRulesRepository) {
        if (pointRulesCode != null) {
            return pointRulesRepository.findPointRulesByPointRulesCode(pointRulesCode).orElseThrow(() -> new PointRulesDoesNotExistException("Point rules with code: " + pointRulesCode + " does not exist!"));
        }
        return null;
    }
}
