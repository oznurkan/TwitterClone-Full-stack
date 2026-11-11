package com.myproject.twitter.repository;

import com.myproject.twitter.dto.response.UserProfileResponseDto;
import com.myproject.twitter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.nickName = :nickName")
    Optional<User> findByNickName(@Param("nickName") String nickName);

    @Query("""
        SELECT new com.myproject.twitter.dto.response.UserProfileResponseDto(
            u.nickName,
            u.firstName,
            u.lastName,
            u.email,
            u.bio,
            u.createdAt,
            u.updatedAt,
            (SELECT COUNT(t) FROM Tweet t WHERE t.user = u),
            (SELECT COUNT(f1) FROM Follow f1 WHERE f1.following = u),
            (SELECT COUNT(f2) FROM Follow f2 WHERE f2.follower = u),
            (COUNT(f3) > 0)
        )
        FROM User u
        LEFT JOIN u.followers f3 ON f3.follower.email = :currentUserEmail
        WHERE u.nickName = :targetUsername
        GROUP BY u.id, u.nickName, u.firstName, u.lastName, u.email, u.bio, u.createdAt, u.updatedAt
    """)
    Optional<UserProfileResponseDto> getUserProfileDtoByUsername(
            @Param("targetUsername") String targetUsername,
            @Param("currentUserEmail") String currentUserEmail
    );

}
