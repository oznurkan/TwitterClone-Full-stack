package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.dto.response.LikeActionResponseDto;
import com.myproject.twitter.dto.response.LikeUserResponseDto;

public interface LikeService {

    LikeActionResponseDto like(Long tweetId);

    LikeActionResponseDto unlike(Long tweetId);

    CursorPageResponseDto<LikeUserResponseDto> getLikesByTweetId(Long tweetId, String cursor, int size);

    boolean isLikedByUser(Long tweetId, Long userId);

    Long countLikes(Long tweetId);

}
