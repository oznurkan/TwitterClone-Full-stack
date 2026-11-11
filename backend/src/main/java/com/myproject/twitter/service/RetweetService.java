package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.RetweetRequestDto;
import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.dto.response.RetweetActionResponseDto;
import com.myproject.twitter.dto.response.RetweetUserResponseDto;

public interface RetweetService {

    RetweetActionResponseDto create(Long tweetId, RetweetRequestDto retweetRequestDto);

    void deleteByTweetId(Long tweetId);

    CursorPageResponseDto<RetweetUserResponseDto> getRetweetersByTweetId(Long tweetId, String cursor, int size);

    boolean isRetweetedByUser(Long tweetId, Long userId);

    Long countRetweets(Long tweetId);
}
