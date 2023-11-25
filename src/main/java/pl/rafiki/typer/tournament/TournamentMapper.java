package pl.rafiki.typer.tournament;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TournamentMapper {
    TournamentMapper INSTANCE = Mappers.getMapper(TournamentMapper.class);

    TournamentDTO tournamentToTournamentDto(Tournament tournament);
    Tournament tournamentDtoToTournament(TournamentDTO tournamentDTO);
}
