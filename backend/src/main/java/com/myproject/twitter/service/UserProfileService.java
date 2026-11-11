package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.*;

public interface UserProfileService {

    CursorPageResponseDto<UserProfilePostResponseDto> getUserPosts(String nickname, String cursor, int size);

    CursorPageResponseDto<TweetResponseDto> getUserTweets(
            String nickname, String cursor, int size
    );


    CursorPageResponseDto<TweetResponseDto> getUserLikedTweets(
            String nickname, String cursor, int size
    );

    CursorPageResponseDto<UserProfileRetweetResponseDto> getUserRetweets(
            String nickname, String cursor, int size
    );

    CursorPageResponseDto<TweetResponseDto> getUserBookmarkedTweets(String cursor, int size) ;

    CursorPageResponseDto<UserProfileCommentResponseDto> getUserComments(String nickname, String cursor, int size);
}
