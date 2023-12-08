package pl.rafiki.typer.match;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.tournament.TournamentRepository;
import pl.rafiki.typer.tournament.exceptions.TournamentDoesNotExistException;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(source = "tournament", target = "tournamentCode", qualifiedByName = "tournamentToTournamentCode")
    MatchDTO matchToMatchDto(Match match);

    @Mapping(source = "tournamentCode", target = "tournament", qualifiedByName = "tournamentCodeToTournament")
    Match matchDtoToMatch(MatchDTO matchDTO, @Context TournamentRepository tournamentRepository);

    @Mapping(source = "tournamentCode", target = "tournament", qualifiedByName = "tournamentCodeToTournament")
    Match addMatchDtoToMatch(AddMatchDTO addMatchDTO, @Context TournamentRepository tournamentRepository);

    @Named("tournamentToTournamentCode")
    default String tournamentToTournamentCode(Tournament tournament) {
        return tournament.getTournamentCode();
    }

    @Named("tournamentCodeToTournament")
    default Tournament tournamentCodeToTournament(String tournamentCode, @Context TournamentRepository tournamentRepository) {
        if (tournamentCode != null) {
            return tournamentRepository.findByTournamentCode(tournamentCode).orElseThrow(() -> new TournamentDoesNotExistException("Tournament with code: " + tournamentCode + " does not exist!"));
        }
        return null;
    }
}