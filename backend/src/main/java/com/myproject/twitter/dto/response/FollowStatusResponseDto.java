package com.myproject.twitter.dto.response;

public record FollowStatusResponseDto(

        Boolean isFollowing,
        Long followerCount
) {
}
