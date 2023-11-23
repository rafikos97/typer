package pl.rafiki.typer.scoreboard;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.user.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScoreboardMapper {
    ScoreboardMapper INSTANCE = Mappers.getMapper(ScoreboardMapper.class);

    @Mapping(source = "tournament", target = "tournamentId", qualifiedByName = "tournamentToTournamentId")
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    ScoreboardDTO scoreboardToScoreboardDto(Scoreboard scoreboard);

    @Named("tournamentToTournamentId")
    default Long tournamentToTournamentId(Tournament tournament) {
        return tournament.getId();
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }
}
