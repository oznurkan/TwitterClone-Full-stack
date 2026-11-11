package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Tweet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query("SELECT t FROM Tweet t JOIN FETCH t.user " +
            "WHERE LOWER(t.content) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "ORDER BY t.createdAt DESC, t.id DESC")
    List<Tweet> searchFirstPageByContent(@Param("text") String text, Pageable pageable);

    @Query("SELECT t FROM Tweet t JOIN FETCH t.user " +
            "WHERE LOWER(t.content) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND (t.createdAt < :cursorDate OR (t.createdAt = :cursorDate AND t.id < :cursorId)) " +
            "ORDER BY t.createdAt DESC, t.id DESC")
    List<Tweet> searchNextPageByContent(@Param("text") String text,
                                        @Param("cursorDate") LocalDateTime cursorDate,
                                        @Param("cursorId") Long cursorId,
                                        Pageable pageable);

    @Query("SELECT t FROM Tweet t JOIN FETCH t.user ORDER BY t.createdAt DESC, t.id DESC")
    List<Tweet> findFirstPage(Pageable pageable);

    @Query("SELECT t FROM Tweet t JOIN FETCH t.user " +
            "WHERE (t.createdAt < :cursorDate OR (t.createdAt = :cursorDate AND t.id < :cursorId)) " +
            "ORDER BY t.createdAt DESC, t.id DESC")
    List<Tweet> findNextPage(@Param("cursorDate") LocalDateTime cursorDate,
                             @Param("cursorId") Long cursorId,
                             Pageable pageable);

    @Query("SELECT t FROM Tweet t JOIN FETCH t.user WHERE t.user.id = :userId ORDER BY t.createdAt DESC, t.id DESC")
    List<Tweet> findFirstPageByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Tweet t JOIN FETCH t.user WHERE t.user.id = :userId " +
            "AND (t.createdAt < :cursorDate OR (t.createdAt = :cursorDate AND t.id < :cursorId)) " +
            "ORDER BY t.createdAt DESC, t.id DESC")
    List<Tweet> findNextPageByUserId(
            @Param("userId") Long userId,
            @Param("cursorDate") LocalDateTime cursorDate,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );

}
