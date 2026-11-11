package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.request.RegisterRequestDto;
import com.myproject.twitter.dto.response.AuthResponseDto;
import com.myproject.twitter.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class AuthMapper {

    public AuthResponseDto toResponseDto(User user) {

        return new AuthResponseDto(
                user.getNickName(),
                formatName(user.getFirstName()),
                formatName(user.getLastName()),
                user.getEmail(),
                user.getBio(),
                user.getCreatedAt()
        );
    }

    public User toEntity(RegisterRequestDto registerRequestDto){

        User user = new User();

        user.setFirstName(registerRequestDto.firstName() != null ? registerRequestDto.firstName().toLowerCase(Locale.ROOT).trim() : null);
        user.setLastName(registerRequestDto.lastName() != null ? registerRequestDto.lastName().toLowerCase(Locale.ROOT).trim() : null);
        user.setNickName(registerRequestDto.nickName().trim());
        user.setEmail(registerRequestDto.email().trim());
        user.setBio(registerRequestDto.bio());
        user.setCreatedAt(LocalDateTime.now());

        return user;

    }

    private String formatName(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1).toLowerCase(Locale.ROOT);
    }


}
