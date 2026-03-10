package ma.jobintech.projetfilrouge.user.mapper;

import ma.jobintech.projetfilrouge.user.dto.response.UserResponseDTO;
import ma.jobintech.projetfilrouge.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDto(User user);
}