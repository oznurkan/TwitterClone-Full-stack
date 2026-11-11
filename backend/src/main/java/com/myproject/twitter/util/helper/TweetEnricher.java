package com.myproject.twitter.util.helper;

import com.myproject.twitter.dto.response.TweetResponseDto;
import com.myproject.twitter.entity.Tweet;
import com.myproject.twitter.service.BookmarkService;
import com.myproject.twitter.service.CommentService;
import com.myproject.twitter.service.LikeService;
import com.myproject.twitter.service.RetweetService;
import com.myproject.twitter.util.mapper.TweetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TweetEnricher {

    private final LikeService likeService;
    private final CommentService commentService;
    private final RetweetService retweetService;
    private final BookmarkService bookmarkService;
    private final TweetMapper tweetMapper;

    public TweetResponseDto enrich(Tweet tweet, Long currentUserId) {
        Long likeCount = likeService.countLikes(tweet.getId());
        Long commentCount = commentService.countComments(tweet.getId());
        Long retweetCount = retweetService.countRetweets(tweet.getId());
        Long bookmarkCount = bookmarkService.countBookmarks(tweet.getId());

        boolean isLikedByMe = currentUserId != null
                && likeService.isLikedByUser(tweet.getId(), currentUserId);
        boolean isRetweetedByMe = currentUserId != null
                && retweetService.isRetweetedByUser(tweet.getId(), currentUserId);
        boolean isBookmarkedByMe = currentUserId != null
                && bookmarkService.isBookmarkedByUser(tweet.getId(), currentUserId);

        return tweetMapper.toResponseDto(
                tweet, likeCount, commentCount, retweetCount, bookmarkCount, isLikedByMe, isRetweetedByMe, isBookmarkedByMe
        );
    }
}