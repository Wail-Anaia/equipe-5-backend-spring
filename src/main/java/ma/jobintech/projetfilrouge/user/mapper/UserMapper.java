package ma.jobintech.projetfilrouge.user.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import ma.jobintech.projetfilrouge.user.dto.response.UserResponse;
import ma.jobintech.projetfilrouge.user.entity.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}