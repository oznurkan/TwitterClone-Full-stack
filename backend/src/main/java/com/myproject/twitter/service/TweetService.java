package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.TweetPatchRequestDto;
import com.myproject.twitter.dto.request.TweetRequestDto;
import com.myproject.twitter.dto.response.*;
import java.util.List;

public interface TweetService {

    CursorPageResponseDto<TweetResponseDto> getFeedTweets(String cursor, int size);

    TweetResponseDto create(TweetRequestDto tweetRequestDto);

    TweetResponseDto findById(Long id);

    TweetResponseDto update(Long tweetId, TweetPatchRequestDto tweetPatchRequestDto);

    TweetResponseDto replaceOrCreate(Long tweetId, TweetRequestDto tweetRequestDto);

    void deleteById(Long tweetId);

    CursorPageResponseDto<TweetResponseDto> searchTweetByContent(String text, String cursor, int size);

}
