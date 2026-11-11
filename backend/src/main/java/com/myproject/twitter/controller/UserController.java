package com.myproject.twitter.controller;

import com.myproject.twitter.dto.request.UserPatchRequestDto;
import com.myproject.twitter.dto.response.*;
import com.myproject.twitter.service.UserProfileService;
import com.myproject.twitter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public UserResponseDto getCurrentUser() {

        return userService.getUserByEmail();
    }

    @GetMapping("/me/bookmarks")
    public CursorPageResponseDto<TweetResponseDto> getUserBookmarkedTweets(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return userProfileService.getUserBookmarkedTweets(cursor, size);
    }

    @PatchMapping("/me/settings")
    public UserResponseDto updateProfile(
            @RequestBody UserPatchRequestDto userPatchRequestDto) {

        return userService.updateProfile(userPatchRequestDto);
    }

    @GetMapping("/{nickName}")
    public UserProfileResponseDto getUserProfile(@PathVariable("nickName") String nickName) {

        return userService.getUserByNickName(nickName);
    }

    @GetMapping("/{nickname}/posts")
    public CursorPageResponseDto<UserProfilePostResponseDto> getUserPosts(
            @PathVariable("nickname") String nickname,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return userProfileService.getUserPosts(nickname, cursor, size);
    }

    @GetMapping("/{nickname}/tweets")
    public CursorPageResponseDto<TweetResponseDto> getUserTweets(
            @PathVariable("nickname") String nickname,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return userProfileService.getUserTweets(nickname, cursor, size);
    }

    @GetMapping("/{nickname}/retweets")
    public CursorPageResponseDto<UserProfileRetweetResponseDto> getUserRetweets(
            @PathVariable("nickname") String nickname,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return userProfileService.getUserRetweets(nickname, cursor, size);
    }

    @GetMapping("/{nickname}/likes")
    public CursorPageResponseDto<TweetResponseDto> getUserLikedTweets(
            @PathVariable("nickname") String nickname,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return userProfileService.getUserLikedTweets(nickname, cursor, size);
    }

    @GetMapping("/{nickname}/comments")
    public CursorPageResponseDto<UserProfileCommentResponseDto> getUserCommentTweets(
            @PathVariable("nickname") String nickname,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return userProfileService.getUserComments(nickname, cursor, size);
    }

}
