package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.response.FollowUserResponseDto;
import com.myproject.twitter.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {

    public FollowUserResponseDto toFollowUserResponseDto(User user, boolean followedByCurrentUser) {

        if( user == null){
            return null;
        }

        String nickName = (user.getNickName() != null) ? user.getNickName() : null ;
        String firstName = (user.getFirstName() != null) ? user.getFirstName() : null ;
        String lastName = (user.getLastName() != null) ? user.getLastName() : null ;

        return new FollowUserResponseDto(
                nickName,
                firstName,
                lastName,
                followedByCurrentUser
        );
    }
}