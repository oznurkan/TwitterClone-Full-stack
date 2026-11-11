package com.myproject.twitter.controller;

import com.myproject.twitter.dto.response.CommentResponseDto;
import com.myproject.twitter.service.CommentService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentSearchController {

    private final CommentService commentService;

    @GetMapping("/search")
    public List<CommentResponseDto> search(
            @Size(min = 3, max = 15, message = "Arama kelimesi 3 ile 15 karakter arasında olmalıdır.")
            @RequestParam("content") String content) {

        return commentService.searchByContent(content);
    }
}