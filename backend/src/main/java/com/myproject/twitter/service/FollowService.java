package com.myproject.twitter.service;

import com.myproject.twitter.dto.response.FollowStatusResponseDto;
import com.myproject.twitter.dto.response.FollowUserResponseDto;

import java.util.List;

public interface FollowService {

    FollowStatusResponseDto follow(String targetNickName);

    FollowStatusResponseDto unfollow(String targetNickName);

    List<FollowUserResponseDto> getFollowings(String targetNickName);

    List<FollowUserResponseDto> getFollowers(String targetNickName);

    boolean isFollowing(Long followerId, Long followingId);

    Long countFollowers(Long userId);

    Long countFollowings(Long userId);
}
