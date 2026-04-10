package com.eai.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eai.user.dto.UserEventDTO;
import com.eai.user.entities.UserEventEntity;

public interface UserEventRepository extends JpaRepository<UserEventEntity,Long> {

  public Optional<UserEventEntity> findUserEventEntityById(Long id);

  
  
//   @Query(
//     value = """
//         select e 
//         from UserEventEntity e 
//         where e.user.idUser = :idUser 
//         order by e.crtAt desc
//     """,
//     countQuery = """
//         select count(e) 
//         from UserEventEntity e 
//         where e.user.idUser = :idUser
//     """
// ) 
@Query("select e from UserEventEntity e where e.user.idUser =:idUser order by e.crtAt desc")
public Page<UserEventEntity> findUserEventEntityByUserId(@Param("idUser")Long idUser,Pageable page);

 
  // @Query(value = "select ue.* from user_event ue"+ 
  //  " join app_user au on au.id_user = ue.user_id "+ 
  //  "where au.id_user  =:idUser order by crt_at desc",
  //   nativeQuery=true)
   @Query("select e from UserEventEntity e where e.user.idUser =:idUser order by e.crtAt desc limit 10")
  public Optional<List<UserEventEntity>> findUserEventsByUserId(@Param("idUser") Long idUser);
}
