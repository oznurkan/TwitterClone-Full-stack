package com.myproject.twitter.dto.response;

public record LikeActionResponseDto(

    Long tweetId,

    String nickName,

    Boolean isLikedByCurrentUser

) {
}
