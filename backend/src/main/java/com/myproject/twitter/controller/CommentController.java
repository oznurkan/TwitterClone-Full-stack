package com.myproject.twitter.controller;

import com.myproject.twitter.dto.request.CommentPatchRequestDto;
import com.myproject.twitter.dto.request.CommentRequestDto;
import com.myproject.twitter.dto.response.CommentResponseDto;
import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/tweets/{tweetId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto create(
            @Positive(message = "Yorum için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId,
            @Valid @RequestBody CommentRequestDto commentRequestDto){

        return commentService.create(tweetId, commentRequestDto);
    }

    @GetMapping("/{commentId}")
    public CommentResponseDto findById(
            @Positive(message = "Yorum için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId,
            @Positive(message = "Yorum için commentId pozitif olmalıdır.") @PathVariable("commentId") Long commentId){

        return commentService.findById(tweetId, commentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorPageResponseDto<CommentResponseDto> getCommentsByTweetId(
            @Positive(message = "Tweet id pozitif olmalıdır.") @PathVariable Long tweetId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size) {

        return commentService.getCommentsByTweetId(tweetId, cursor, size);
    }

    @PatchMapping("/{commentId}")
    public CommentResponseDto update(
            @Positive(message = "Yorum için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId,
            @Positive(message = "Yorum için commentId pozitif olmalıdır.") @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentPatchRequestDto commentPatchRequestDto){

        return commentService.update(tweetId, commentId, commentPatchRequestDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Positive(message = "Yorum için tweetId pozitif olmalıdır.") @PathVariable("tweetId") Long tweetId,
            @Positive(message = "Yorum için commentId pozitif olmalıdır.") @PathVariable("commentId") Long commentId){

        commentService.deleteById(tweetId, commentId);
    }
}
