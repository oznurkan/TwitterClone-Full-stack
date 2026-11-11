package com.myproject.twitter.dto.response;

public record FollowUserResponseDto(

        String nickName,

        String firstName,

        String lastName,

        Boolean followedByCurrentUser
) {
}
