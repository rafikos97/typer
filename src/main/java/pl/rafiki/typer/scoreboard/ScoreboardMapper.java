package pl.rafiki.typer.scoreboard;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.rafiki.typer.user.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScoreboardMapper {
    ScoreboardMapper INSTANCE = Mappers.getMapper(ScoreboardMapper.class);

    @Mapping(source = "user", target = "userFirstName", qualifiedByName = "userToUserFirstName")
    @Mapping(source = "user", target = "userLastName", qualifiedByName = "userToUserLastName")
    ScoreboardDTO scoreboardToScoreboardDto(Scoreboard scoreboard);

    @Named("userToUserFirstName")
    default String userToUserFirstName(User user) {
        return user.getFirstName();
    }

    @Named("userToUserLastName")
    default String userToUserLastName(User user) {
        return user.getLastName();
    }
}
