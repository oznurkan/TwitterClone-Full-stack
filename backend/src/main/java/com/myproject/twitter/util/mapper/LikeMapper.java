package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.response.LikeActionResponseDto;
import com.myproject.twitter.dto.response.LikeUserResponseDto;
import com.myproject.twitter.entity.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {

    public LikeActionResponseDto toResponseDto(Like like, boolean isLiked){

        if (like == null) {
            return null;
        }

        String nickName = (like.getUser() != null) ? like.getUser().getNickName() : null;
        Long tweetId = (like.getTweet() != null) ? like.getTweet().getId() : null;

        return new LikeActionResponseDto(
                tweetId,
                nickName,
                isLiked
        );

    }

    public LikeUserResponseDto toUserResponseDto(Like like, boolean isFollowing){

        if (like == null) {
            return null;
        }

        String firstName = (like.getUser() != null) ? like.getUser().getFirstName() : null;
        String lastName = (like.getUser() != null) ? like.getUser().getLastName() : null;
        String nickName = (like.getUser() != null) ? like.getUser().getNickName() : null;

        return new LikeUserResponseDto(
                nickName,
                firstName,
                lastName,
                isFollowing
        );

    }

}
