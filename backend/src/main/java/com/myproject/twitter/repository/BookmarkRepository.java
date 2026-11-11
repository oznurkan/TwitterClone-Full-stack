package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Bookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndTweetId( Long userId, Long tweetId);

    Long countByTweetId(Long tweetId);

    void deleteByUserIdAndTweetId(Long userId, Long tweetId);

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.tweet t JOIN FETCH t.user " +
            "WHERE b.user.id = :userId " +
            "ORDER BY b.createdAt DESC, b.id DESC")
    List<Bookmark> findFirstPageByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.tweet t JOIN FETCH t.user " +
            "WHERE b.user.id = :userId " +
            "AND (b.createdAt < :cursorDate OR (b.createdAt = :cursorDate AND b.id < :cursorId)) " +
            "ORDER BY b.createdAt DESC, b.id DESC")
    List<Bookmark> findNextPageByUserId(@Param("userId") Long userId,
                                        @Param("cursorDate") LocalDateTime cursorDate,
                                        @Param("cursorId") Long cursorId,
                                        Pageable pageable);

}
