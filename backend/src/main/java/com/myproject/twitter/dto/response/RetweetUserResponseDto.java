package com.myproject.twitter.dto.response;

public record RetweetUserResponseDto(

        String nickName,

        String firstName,

        String lastName,

        Boolean isFollowingByCurrentUser
) {
}
