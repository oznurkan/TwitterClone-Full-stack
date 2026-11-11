package com.myproject.twitter.controller;

import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.dto.response.LikeActionResponseDto;
import com.myproject.twitter.dto.response.LikeUserResponseDto;
import com.myproject.twitter.service.LikeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/tweets/{tweetId}")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeActionResponseDto like(@Positive(message = "Like için tweetId pozitif olmalıdır.") @PathVariable Long tweetId) {

        return likeService.like(tweetId);
    }

    @PostMapping("/unlike")
    @ResponseStatus(HttpStatus.OK)
    public LikeActionResponseDto unlike(@Positive(message = "Like için tweetId pozitif olmalıdır.") @PathVariable Long tweetId) {

        return likeService.unlike(tweetId);
    }

    @GetMapping("/likes")
    @ResponseStatus(HttpStatus.OK)
    public CursorPageResponseDto<LikeUserResponseDto> getLikesByTweetId(
            @Positive(message = "Like için tweetId pozitif olmalıdır.") @PathVariable Long tweetId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return likeService.getLikesByTweetId(tweetId, cursor, size);
    }

}
