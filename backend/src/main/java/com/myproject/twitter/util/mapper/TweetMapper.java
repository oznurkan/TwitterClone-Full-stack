package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.request.TweetPatchRequestDto;
import com.myproject.twitter.dto.request.TweetRequestDto;
import com.myproject.twitter.dto.response.TweetResponseDto;
import com.myproject.twitter.entity.Tweet;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class TweetMapper {

    public TweetResponseDto toResponseDto(Tweet tweet, Long likeCount, Long commentCount, Long retweetCount, Long bookmarkCount, Boolean isLikedByMe, Boolean isRetweetedByMe, Boolean isBookmarkedByMe) {
        return new TweetResponseDto(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUpdatedAt(),
                tweet.getUser() != null ? tweet.getUser().getNickName() : "Unknown NickName",
                tweet.getUser() != null ? tweet.getUser().getFirstName() : "Unknown FirstName",
                tweet.getUser() != null ? tweet.getUser().getLastName() : "Unknown LastName",
                likeCount,
                commentCount,
                retweetCount,
                bookmarkCount,
                isLikedByMe,
                isRetweetedByMe,
                isBookmarkedByMe
        );
    }

    public Tweet toEntity(TweetRequestDto tweetRequestDto){

        Tweet tweet = new Tweet();
        tweet.setContent(tweetRequestDto.content());

        if( tweet.getCreatedAt() == null){
            tweet.setCreatedAt(LocalDateTime.now());
        }else{
            tweet.setUpdatedAt(LocalDateTime.now());
        }

        return tweet;
    }

    public void updateEntity(Tweet updateTweet, TweetPatchRequestDto tweetPatchRequestDto){

        if( tweetPatchRequestDto.content() != null){
            updateTweet.setContent(tweetPatchRequestDto.content());
        }

        updateTweet.setUpdatedAt(LocalDateTime.now());
    }
}
