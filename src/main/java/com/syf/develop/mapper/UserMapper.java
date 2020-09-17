package com.syf.develop.mapper;

import com.syf.develop.entity.User;
import com.syf.develop.model.CreateUserRequest;
import com.syf.develop.model.CreateUserResponse;
import com.syf.develop.model.UserDTO;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UserMapper {

    public CreateUserResponse mapDtotoResponse(UserDTO userDto) {
        return CreateUserResponse.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public UserDTO mapRequestToDto(CreateUserRequest createUserRequest) {
        return UserDTO.builder()
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .enabled(false)
                .createdDate(Instant.now())
                .build();
    }

    public UserDTO userToUserDTO(User savedUser) {
        return UserDTO.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }

    public User userDtoToUser(UserDTO request) {
        return User.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .encryptedPassword(request.getEncryptedPassword())
                .createdDate(request.getCreatedDate())
                .enabled(request.isEnabled())
                .build();
    }
}
