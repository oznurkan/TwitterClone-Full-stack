package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Retweet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);

    Long countByTweetId(Long tweetId);

    void deleteByUserIdAndTweetId(Long userId, Long tweetId);

    @Query("SELECT r FROM Retweet r JOIN FETCH r.user u " +
            "WHERE r.tweet.id = :tweetId " +
            "ORDER BY r.createdAt DESC, r.id DESC")
    List<Retweet> findFirstPageByTweetId(@Param("tweetId") Long tweetId, Pageable pageable);

    @Query("SELECT r FROM Retweet r JOIN FETCH r.user u " +
            "WHERE r.tweet.id = :tweetId AND " +
            "(r.createdAt < :cursorDate OR (r.createdAt = :cursorDate AND r.id < :lastId)) " +
            "ORDER BY r.createdAt DESC, r.id DESC")
    List<Retweet> findNextPageByTweetId(@Param("tweetId") Long tweetId,
                                        @Param("cursorDate") LocalDateTime cursorDate,
                                        @Param("lastId") Long lastId,
                                        Pageable pageable);

    @Query("SELECT r FROM Retweet r JOIN FETCH r.tweet t JOIN FETCH t.user " +
            "WHERE r.user.id = :userId " +
            "ORDER BY r.createdAt DESC, r.id DESC")
    List<Retweet> findFirstPageByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT r FROM Retweet r JOIN FETCH r.tweet t JOIN FETCH t.user " +
            "WHERE r.user.id = :userId " +
            "AND (r.createdAt < :cursorDate OR (r.createdAt = :cursorDate AND r.id < :cursorId)) " +
            "ORDER BY r.createdAt DESC, r.id DESC")
    List<Retweet> findNextPageByUserId(
            @Param("userId") Long userId,
            @Param("cursorDate") LocalDateTime cursorDate,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

}
