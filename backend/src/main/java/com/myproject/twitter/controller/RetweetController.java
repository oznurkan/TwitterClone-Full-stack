package com.myproject.twitter.controller;

import com.myproject.twitter.dto.request.RetweetRequestDto;
import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.dto.response.RetweetActionResponseDto;
import com.myproject.twitter.dto.response.RetweetUserResponseDto;
import com.myproject.twitter.service.RetweetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/tweets/{tweetId}/retweets")
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RetweetActionResponseDto create(
            @Positive(message = "Retweet için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId,
            @Valid @RequestBody RetweetRequestDto retweetRequestDto){

        return retweetService.create(tweetId, retweetRequestDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive(message = "Retweet için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId){

        retweetService.deleteByTweetId(tweetId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorPageResponseDto<RetweetUserResponseDto> getRetweetsByTweetId(
            @Positive(message = "Retweet için tweetId pozitif olmalıdır.") @PathVariable Long tweetId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return retweetService.getRetweetersByTweetId(tweetId, cursor, size);
    }
}
