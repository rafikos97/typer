package pl.rafiki.typer.pointrules;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PointRulesMapper {
    PointRulesMapper INSTANCE = Mappers.getMapper(PointRulesMapper.class);

    PointRulesDTO mapPointRulesToPointRulesDto(PointRules pointRules);
    PointRules mapPointRulesDtoToPointRules(PointRulesDTO pointRulesDTO);
}
