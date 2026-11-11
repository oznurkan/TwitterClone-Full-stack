package com.myproject.twitter.dto.response;

import java.time.LocalDateTime;

public record TweetResponseDto(

        Long tweetId,
        String content,
        LocalDateTime updatedAt,
        String nickName,
        String firstName,
        String lastName,
        Long likeCount,
        Long commentCount,
        Long retweetCount,
        Long bookmarkCount,
        Boolean likedByCurrentUser,
        Boolean retweetedByCurrentUser,
        Boolean bookmarkedByCurrentUser

) {
}
