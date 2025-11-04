package com.eai.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eai.user.entities.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

   @Query("select  c from CustomerEntity c where c.addressEntity.idAddress =:idAddress")
   Optional<List<CustomerEntity>> findCustomersByIdAddress(@Param("idAddress") long idAddress);

   @Query("select c.configJson from CustomerEntity c where c.email =:email")
   Optional<String> findJsonFile(@Param("email") String email);

   public Optional<CustomerEntity> findByEmail(String email);
}
