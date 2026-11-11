package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.request.RetweetRequestDto;
import com.myproject.twitter.dto.response.RetweetActionResponseDto;
import com.myproject.twitter.dto.response.RetweetUserResponseDto;
import com.myproject.twitter.entity.Retweet;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class RetweetMapper {

    public RetweetActionResponseDto toResponseDto(Retweet retweet, Boolean isRetweeted){

        if (retweet == null) {
            return null;
        }

        String nickName = (retweet.getUser() != null) ? retweet.getUser().getNickName() : null;
        Long tweetId = (retweet.getTweet() != null) ? retweet.getTweet().getId() : null;

        return new RetweetActionResponseDto(
                tweetId,
                nickName,
                retweet.getText(),
                retweet.getCreatedAt(),
                isRetweeted
        );

    }

    public RetweetUserResponseDto toUserResponseDto(Retweet retweet, Boolean isFollowingByCurrentUser){

        if (retweet == null) {
            return null;
        }

        String nickName = (retweet.getUser() != null) ? retweet.getUser().getNickName() : null;
        String firstName = (retweet.getUser() != null) ? retweet.getUser().getFirstName() : null;
        String lastName = (retweet.getUser() != null) ? retweet.getUser().getLastName() : null;

        return new RetweetUserResponseDto(
                nickName,
                firstName,
                lastName,
                isFollowingByCurrentUser
        );

    }

    public Retweet toEntity(RetweetRequestDto retweetRequestDto){

        if (retweetRequestDto == null) {
            return null;
        }

        Retweet retweet = new Retweet();
        retweet.setText(retweetRequestDto.text());
        retweet.setCreatedAt(LocalDateTime.now());

        return retweet;
    }

}
