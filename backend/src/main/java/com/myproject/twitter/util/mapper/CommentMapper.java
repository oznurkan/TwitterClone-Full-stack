package com.myproject.twitter.util.mapper;

import com.myproject.twitter.dto.request.CommentPatchRequestDto;
import com.myproject.twitter.dto.request.CommentRequestDto;
import com.myproject.twitter.dto.response.CommentResponseDto;
import com.myproject.twitter.entity.Comment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public CommentResponseDto toResponseDto(Comment comment){

        if( comment == null){
            return null;
        }

        Long commentId = (comment.getId() != null ) ? comment.getId() : null;
        Long tweetId = (comment.getTweet() != null) ? comment.getTweet().getId() : null;
        String firstName = (comment.getUser() != null ) ? comment.getUser().getFirstName() : null;
        String lastName = (comment.getUser() != null ) ? comment.getUser().getLastName() : null;
        String nickName = (comment.getUser() != null ) ? comment.getUser().getNickName() : null;
        String content = (comment.getContent() != null ) ? comment.getContent() : null;
        LocalDateTime updateAt = (comment.getUpdatedAt() != null ) ? comment.getUpdatedAt() : null;

        return new CommentResponseDto(
                commentId,
                tweetId,
                firstName,
                lastName,
                nickName,
                content,
                updateAt
        );
    }

    public Comment toEntity(CommentRequestDto commentRequestDto){

        Comment comment = new Comment();

        comment.setContent(commentRequestDto.content());

        if( comment.getCreatedAt() == null){
            comment.setCreatedAt(LocalDateTime.now());
        }else{
            comment.setUpdatedAt(LocalDateTime.now());
        }

        return comment;

    }

    public void updateEntity(Comment updatedComment, CommentPatchRequestDto commentPatchRequestDto){

        if( commentPatchRequestDto.content() != null){
            updatedComment.setContent(commentPatchRequestDto.content());
        }

        updatedComment.setUpdatedAt(LocalDateTime.now());
    }
}
