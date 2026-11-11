package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.CommentPatchRequestDto;
import com.myproject.twitter.dto.request.CommentRequestDto;
import com.myproject.twitter.dto.response.CommentResponseDto;
import com.myproject.twitter.dto.response.CursorPageResponseDto;
import com.myproject.twitter.entity.Comment;
import com.myproject.twitter.entity.Tweet;
import com.myproject.twitter.entity.User;
import com.myproject.twitter.exception.BadRequestException;
import com.myproject.twitter.exception.CommentNotFoundException;
import com.myproject.twitter.exception.TweetNotFoundException;
import com.myproject.twitter.repository.CommentRepository;
import com.myproject.twitter.repository.TweetRepository;
import com.myproject.twitter.security.AuthUtils;
import com.myproject.twitter.util.mapper.CommentMapper;
import com.myproject.twitter.util.pagination.CursorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final AuthUtils authUtils;

    private final CommentRepository commentRepository;

    private final TweetRepository tweetRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto findById(Long tweetId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment bulunamadı, id: " + commentId));

        if (!comment.getTweet().getId().equals(tweetId)) {
            throw new BadRequestException("Bu yorum bu tweet'e ait değil!");
        }

        return commentMapper.toResponseDto(comment);
    }


    @Override
    @Transactional
    public CommentResponseDto update(Long tweetId, Long commentId, CommentPatchRequestDto commentPatchRequestDto){

        Comment commentToUpdate = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("comment bulunumadı, id: " + commentId));

        authUtils.verifyOwnership(commentToUpdate.getUser().getId());

        if (tweetId != null && !commentToUpdate.getTweet().getId().equals(tweetId)) {
            throw new BadRequestException("Bu yorum bu tweet'e ait değil!");
        }


        commentMapper.updateEntity(commentToUpdate, commentPatchRequestDto);

        Comment savedComment = commentRepository.save(commentToUpdate);

        return commentMapper.toResponseDto(savedComment);


    }

    @Override
    @Transactional
    public CommentResponseDto create(Long tweetId, CommentRequestDto commentRequestDto) {

        User currentUser = authUtils.getCurrentUserReference();

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TweetNotFoundException("Yorum için tweet bulunamadı"));

        Comment comment = commentMapper.toEntity(commentRequestDto);
        comment.setTweet(tweet);
        comment.setUser(currentUser);

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponseDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteById(Long tweetId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Yorum bulunamadı, id: " + commentId));

        if (!comment.getTweet().getId().equals(tweetId)) {
            throw new BadRequestException("Bu yorum bu tweet'e ait değil!");
        }

        authUtils.verifyCommentDeletionAuth(
                comment.getUser().getId(),
                comment.getTweet().getUser().getId()
        );

        commentRepository.deleteById(commentId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> searchByContent(String content) {
        return commentRepository
                .searchByContent(content)
                .stream()
                .map(commentMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countComments(Long tweetId){

        return commentRepository.countByTweetId(tweetId);
    }

    @Override
    @Transactional(readOnly = true)
    public CursorPageResponseDto<CommentResponseDto> getCommentsByTweetId(Long tweetId, String cursor, int size) {
        if (!tweetRepository.existsById(tweetId)) {
            throw new TweetNotFoundException("Tweet bulunamadı: " + tweetId);
        }

        CursorUtils.CursorData cursorData = CursorUtils.decode(cursor);
        Pageable pageable = PageRequest.of(0, size + 1);

        List<Comment> comments;
        if (cursorData.cursorDate() == null || cursorData.lastId() == null) {
            comments = commentRepository.findFirstPageByTweetId(tweetId, pageable);
        } else {
            comments = commentRepository.findNextPageByTweetId(tweetId, cursorData.cursorDate(), cursorData.lastId(), pageable);
        }

        boolean hasNext = comments.size() > size;
        List<Comment> content = hasNext ? new ArrayList<>(comments.subList(0, size)) : comments;

        String nextCursor = null;
        if (!content.isEmpty() && hasNext) {
            Comment lastItem = content.get(content.size() - 1);
            nextCursor = CursorUtils.encode(lastItem.getCreatedAt(), lastItem.getId());
        }

        List<CommentResponseDto> dtoList = content.stream()
                .map(commentMapper::toResponseDto)
                .toList();

        return new CursorPageResponseDto<>(dtoList, nextCursor, hasNext);
    }


}
