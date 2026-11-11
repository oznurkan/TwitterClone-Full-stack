package com.myproject.twitter.dto.response;

public record UserProfileRetweetResponseDto(

        String nickName,

        String text,

        TweetResponseDto tweet
) {
}
