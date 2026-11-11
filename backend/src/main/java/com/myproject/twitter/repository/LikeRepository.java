package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Like;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);

    Long countByTweetId(Long tweetId);

    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);

    @Query("SELECT l FROM Like l JOIN FETCH l.user u " +
            "WHERE l.tweet.id = :tweetId ORDER BY l.createdAt DESC, l.id DESC")
    List<Like> findFirstPageByTweetId(@Param("tweetId") Long tweetId, Pageable pageable);

    @Query("SELECT l FROM Like l JOIN FETCH l.user u " +
            "WHERE l.tweet.id = :tweetId AND " +
            "(l.createdAt < :cursorDate OR (l.createdAt = :cursorDate AND l.id < :lastId)) " +
            "ORDER BY l.createdAt DESC, l.id DESC")
    List<Like> findNextPageByTweetId(@Param("tweetId") Long tweetId,
                                     @Param("cursorDate") LocalDateTime cursorDate,
                                     @Param("lastId") Long lastId,
                                     Pageable pageable);

    @Query("SELECT l FROM Like l JOIN FETCH l.tweet t JOIN FETCH t.user " +
            "WHERE l.user.id = :userId ORDER BY l.createdAt DESC, l.id DESC")
    List<Like> findFirstPageByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT l FROM Like l JOIN FETCH l.tweet t JOIN FETCH t.user " +
            "WHERE l.user.id = :userId AND " +
            "(l.createdAt < :cursorDate OR (l.createdAt = :cursorDate AND l.id < :lastId)) " +
            "ORDER BY l.createdAt DESC, l.id DESC")
    List<Like> findNextPageByUserId(@Param("userId") Long userId,
                                    @Param("cursorDate") LocalDateTime cursorDate,
                                    @Param("lastId") Long lastId,
                                    Pageable pageable);

}
