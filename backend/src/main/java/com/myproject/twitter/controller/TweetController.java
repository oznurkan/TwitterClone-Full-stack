package com.myproject.twitter.controller;

import com.myproject.twitter.dto.request.TweetPatchRequestDto;
import com.myproject.twitter.dto.request.TweetRequestDto;
import com.myproject.twitter.dto.response.*;
import com.myproject.twitter.service.TweetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorPageResponseDto<TweetResponseDto> getAll(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return tweetService.getFeedTweets(cursor, size);
    }

    @GetMapping("/{tweetId}")
    @ResponseStatus(HttpStatus.OK)
    public TweetResponseDto findById(@Positive(message = "Tweet id pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId){

        return tweetService.findById(tweetId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public CursorPageResponseDto<TweetResponseDto> searchTweets(
            @Size(min = 3, max = 15) @RequestParam(name = "text") String text,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return tweetService.searchTweetByContent(text, cursor, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto create(@Valid @RequestBody TweetRequestDto tweetRequestDto){

        return tweetService.create(tweetRequestDto);
    }

    @PutMapping("/{tweetId}")
    public TweetResponseDto replaceOrCreate(@Positive(message = "Tweet id pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId,@Valid @RequestBody TweetRequestDto tweetRequestDto){

        return tweetService.replaceOrCreate(tweetId, tweetRequestDto);
    }

    @PatchMapping("/{tweetId}")
    @ResponseStatus(HttpStatus.OK)
    public TweetResponseDto update(@Positive(message = "Tweet id pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId,@Valid @RequestBody TweetPatchRequestDto tweetPatchRequestDto){

        return tweetService.update(tweetId, tweetPatchRequestDto);
    }

    @DeleteMapping("/{tweetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive(message = "Tweet id pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId){

        tweetService.deleteById(tweetId);
    }

}
