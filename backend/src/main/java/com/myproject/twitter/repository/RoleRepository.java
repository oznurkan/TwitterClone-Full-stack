package com.myproject.twitter.repository;

import com.myproject.twitter.entity.Role;
import com.myproject.twitter.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>  {

    /*@Query("SELECT r FROM Role r WHERE r.authority = :authority")
    Optional<Role> getByAuthority(@Param("authority") String authority);


     */
    @Query("SELECT r FROM Role r WHERE r.authority = :authority")
    Optional<Role> findByAuthority(@Param("authority") RoleType authority);


}
