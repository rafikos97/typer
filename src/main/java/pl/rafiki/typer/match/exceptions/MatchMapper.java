package pl.rafiki.typer.match.exceptions;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchDTO matchToMatchDto(Match match);
}