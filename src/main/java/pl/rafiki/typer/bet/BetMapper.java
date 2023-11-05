package pl.rafiki.typer.bet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.rafiki.typer.match.Match;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BetMapper {
    BetMapper INSTANCE = Mappers.getMapper(BetMapper.class);

    @Mapping(source = "id", target = "betId")
    @Mapping(source = "match", target = "matchId", qualifiedByName = "matchToMatchId")
    BetDTO betToBetDto(Bet bet);

    @Named("matchToMatchId")
    public static Long matchToMatchId(Match match) {
        return match.getId();
    }
}
