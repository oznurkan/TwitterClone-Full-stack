package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
        SELECT c FROM Comment c WHERE LOWER(c.content) LIKE LOWER(CONCAT('%', :content, '%'))
    """)
    List<Comment> searchByContent(@Param("content") String content);

    Long countByTweetId(Long tweetId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.tweet t JOIN FETCH t.user " +
            "WHERE c.user.id = :userId " +
            "ORDER BY c.createdAt DESC, c.id DESC")
    List<Comment> findFirstPageByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c FROM Comment c JOIN FETCH c.tweet t JOIN FETCH t.user " +
            "WHERE c.user.id = :userId " +
            "AND (c.createdAt < :cursorDate OR (c.createdAt = :cursorDate AND c.id < :cursorId)) " +
            "ORDER BY c.createdAt DESC, c.id DESC")
    List<Comment> findNextPageByUserId(
            @Param("userId") Long userId,
            @Param("cursorDate") LocalDateTime cursorDate,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

    @Query("SELECT c FROM Comment c JOIN FETCH c.user u WHERE c.tweet.id = :tweetId ORDER BY c.createdAt DESC, c.id DESC")
    List<Comment> findFirstPageByTweetId(@Param("tweetId") Long tweetId, Pageable pageable);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user u WHERE c.tweet.id = :tweetId AND (c.createdAt < :cursorDate OR (c.createdAt = :cursorDate AND c.id < :cursorId)) ORDER BY c.createdAt DESC, c.id DESC")
    List<Comment> findNextPageByTweetId(
            @Param("tweetId") Long tweetId,
            @Param("cursorDate") LocalDateTime cursorDate,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}
