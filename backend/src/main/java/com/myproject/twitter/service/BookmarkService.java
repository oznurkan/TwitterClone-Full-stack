package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.BookmarkResponseDto;

public interface BookmarkService {

    BookmarkResponseDto create(Long tweetId);

    void deleteByTweetId(Long tweetId);

    boolean isBookmarkedByUser(Long tweetId, Long userId);

    Long countBookmarks(Long tweetId);
}
