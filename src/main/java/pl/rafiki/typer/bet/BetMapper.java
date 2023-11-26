package pl.rafiki.typer.bet;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchRepository;
import pl.rafiki.typer.match.exceptions.MatchDoesNotExistException;
import pl.rafiki.typer.user.User;
import pl.rafiki.typer.user.UserRepository;
import pl.rafiki.typer.user.exceptions.UserDoesNotExistException;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring"
)
public interface BetMapper {
    BetMapper INSTANCE = Mappers.getMapper(BetMapper.class);

    @Mapping(source = "match", target = "matchId", qualifiedByName = "matchToMatchId")
    @Mapping(source = "user", target = "userId", qualifiedByName = "userToUserId")
    BetDTO betToBetDto(Bet bet);

    @Mapping(source = "matchId", target = "match", qualifiedByName = "matchIdToMatch")
    @Mapping(source = "userId", target = "user", qualifiedByName = "userIdToUser")
    Bet betDtoToBet(BetDTO betDTO, @Context MatchRepository matchRepository, @Context UserRepository userRepository);

    @Named("matchToMatchId")
    default Long matchToMatchId(Match match) {
        return match.getId();
    }

    @Named("matchIdToMatch")
    default Match matchIdToMatch(Long matchId, @Context MatchRepository matchRepository) {
        if (matchId != null) {
            return matchRepository.findById(matchId).orElseThrow(() -> new MatchDoesNotExistException("Match with id: " + matchId + " does not exist!"));
        }
        return null;
    }

    @Named("userToUserId")
    default Long userToUserId(User user) {
        return user.getId();
    }

    @Named("userIdToUser")
    default User userIdToUser(Long userId, @Context UserRepository userRepository) {
        if (userId != null) {
            return userRepository.findById(userId).orElseThrow(() -> new UserDoesNotExistException("User with id: " + userId + " does not exist!"));
        }
        return null;
    }
}
