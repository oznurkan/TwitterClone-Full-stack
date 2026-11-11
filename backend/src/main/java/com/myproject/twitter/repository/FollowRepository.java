package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Follow;
import com.myproject.twitter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(
            @Param("followerId") Long followerId,
            @Param("followingId") Long followingId
    );

    boolean existsByFollowerIdAndFollowingId(
            @Param("followerId") Long followerId,
            @Param("followingId") Long followingId
    );

    @Query("SELECT f.following FROM Follow f LEFT JOIN FETCH f.following.roles WHERE f.follower.id = :userId")
    List<User> findFollowingsByUserId(@Param("userId") Long userId);

    @Query("SELECT f.follower FROM Follow f LEFT JOIN FETCH f.follower.roles WHERE f.following.id = :userId")
    List<User> findFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
    Long countFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
    Long countFollowingsByUserId(@Param("userId") Long userId);

}

/*

package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Follow;
import com.myproject.twitter.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {


    @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
    List<Long> findFollowingIdsByUserId(@Param("userId") Long userId);

}
 */
