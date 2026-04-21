package com.eai.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eai.user.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {

   @Query("select u from AppUser u where u.email =:email and u.status in (0,1)")
   Optional<AppUser> findUserByEmail(@Param("email")String email);

   @Modifying
   @Query("update AppUser u set u.imageUrl =:imageUrl where u.idUser =:id")
   void updateImage(@Param("imageUrl")String imageUrl, @Param("id")Long id);
}
