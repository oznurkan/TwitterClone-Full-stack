package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.CommentPatchRequestDto;
import com.myproject.twitter.dto.request.CommentRequestDto;
import com.myproject.twitter.dto.response.CommentResponseDto;
import com.myproject.twitter.dto.response.CursorPageResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto findById(Long tweetId, Long commentId);

    CommentResponseDto update(Long tweetId, Long commentId, CommentPatchRequestDto commentPatchRequestDto);

    CommentResponseDto create(Long tweetId, CommentRequestDto commentRequestDto);

    void deleteById(Long tweetId, Long commentId);

    List<CommentResponseDto> searchByContent(String content);

    Long countComments(Long tweetId);

    CursorPageResponseDto<CommentResponseDto> getCommentsByTweetId(Long tweetId, String cursor, int size);


}
