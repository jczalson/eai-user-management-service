package com.eai.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eai.user.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {

   @Query("select u from AppUser u where u.email =:email and u.userStatusEnum in (0,1)")
   Optional<AppUser> findUserByEmailAndStatus(@Param("email")String email);
}
