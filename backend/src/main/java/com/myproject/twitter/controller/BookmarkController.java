package com.myproject.twitter.controller;

import com.myproject.twitter.dto.response.BookmarkResponseDto;
import com.myproject.twitter.service.BookmarkService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/tweets/{tweetId}/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookmarkResponseDto create(@Positive(message = "Bookmark için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId ){

        return bookmarkService.create(tweetId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive(message = "Bookmark için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId){

        bookmarkService.deleteByTweetId(tweetId);
    }
}
