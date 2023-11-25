package pl.rafiki.typer.user;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDto(User user);
    User userDtoToUser(UserDTO userDTO);
    User registerUserDtoToUser(RegisterUserDTO registerUserDTO);
}
