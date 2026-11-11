package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.request.RegisterRequestDto;
import com.myproject.twitter.dto.request.UserPatchRequestDto;
import com.myproject.twitter.dto.response.UserResponseDto;
import com.myproject.twitter.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UserResponseDto toResponseDto(User user) {

        return new UserResponseDto(
                user.getNickName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBio(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toEntity(RegisterRequestDto registerRequestDto){

        User user = new User();

        user.setFirstName(registerRequestDto.firstName());
        user.setLastName(registerRequestDto.lastName());
        user.setNickName(registerRequestDto.nickName());
        user.setEmail(registerRequestDto.email());
        user.setBio(registerRequestDto.bio());

        user.setCreatedAt(LocalDateTime.now());

        return user;

    }

    public void updateEntity(User updateUser, UserPatchRequestDto userPatchRequestDto){

        if( userPatchRequestDto.nickName() != null){
            updateUser.setNickName(userPatchRequestDto.nickName());
        }

        if( userPatchRequestDto.firstName()!= null){
            updateUser.setFirstName(userPatchRequestDto.firstName());
        }

        if( userPatchRequestDto.lastName()!= null){
            updateUser.setLastName(userPatchRequestDto.lastName());
        }

        if( userPatchRequestDto.password()!= null){
            updateUser.setPassword(userPatchRequestDto.password());
        }

        if( userPatchRequestDto.bio()!= null){
            updateUser.setBio(userPatchRequestDto.bio());
        }


        updateUser.setUpdatedAt(LocalDateTime.now());
    }


}
