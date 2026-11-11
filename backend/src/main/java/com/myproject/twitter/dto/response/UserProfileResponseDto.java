package com.myproject.twitter.dto.response;

import java.time.LocalDateTime;

public record UserProfileResponseDto(

        String nickName,

        String firstName,

        String lastName,

        String email,

        String bio,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Long tweetCount,

        Long followerCount,

        Long followingCount,

        Boolean followedByCurrentUser


) {
}
