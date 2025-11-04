package com.eai.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eai.user.entities.TwoFactorVerificationsEntity;

public interface TwoFactorVerificationsRepository extends JpaRepository<TwoFactorVerificationsEntity, Long> {

  @Query("select c from TwoFactorVerificationsEntity c where c.code = :code")
  public Optional<TwoFactorVerificationsEntity> findUserByCodeVerification(@Param("code")String code);  

  @Query("select c from TwoFactorVerificationsEntity c where c.user.idUser = :idUser")
  public Optional<TwoFactorVerificationsEntity> findCodeVerificationByIdUser(@Param("idUser")long idUser);  

  @Query("select c from TwoFactorVerificationsEntity c where c.user.email = :email and c.code = :code")
  public Optional<TwoFactorVerificationsEntity> findCodeVerificationByIdUserAndCode(@Param("email")String email,
                     @Param("code") String code);  

}
