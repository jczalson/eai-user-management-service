package com.eai.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eai.user.entities.UserEventEntity;

public interface UserEventRepository extends JpaRepository<UserEventEntity,Long> {

  public Optional<UserEventEntity> findUserEventEntityByidUserEvent(Long id);

  @Query("select e from UserEventEntity e where e.user.idUser =:idUser order by e.createAt desc limit 10")
  // @Query(value = "select ue.* from user_event ue join app_user au on au.id_user = ue.user_id where au.id_user  =:idUser order by crt_at desc",
  //   nativeQuery=true
  // )
  public Optional<List<UserEventEntity>> findUserEventsByUserId(@Param("idUser") Long idUser);
}
