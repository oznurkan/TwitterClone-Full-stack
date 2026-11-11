package com.myproject.twitter.dto.response;

public record LikeUserResponseDto(

        String nickName,

        String firstName,

        String lastName,

        Boolean isFollowingByCurrentUser

) {
}
