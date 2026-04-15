package com.eai.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eai.user.dto.CustomerDTO;
import com.eai.user.entities.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

   @Query("select  c from CustomerEntity c where c.address.idAddress =:idAddress")
   Optional<List<CustomerEntity>> findCustomersByIdAddress(@Param("idAddress") long idAddress);

   @Query("select c.configJson from CustomerEntity c where c.email =:email")
   Optional<String> findJsonFile(@Param("email") String email);

   public Optional<CustomerEntity> findByEmail(String email);

  //  Page<CustomerEntity> findByNameContaining(String name, Pageable page);
//WIth Page it's better to use Spring Data JPA DTO Projections
  @Query("select new com.eai.user.dto.CustomerDTO(c.idCustomer, c.nm, c.email," + 
        " c.createdDt, c.customerType,a.idAddress, a.streetName,a.city) from CustomerEntity c  "+
      " join AddressEntity a on c.address.idAddress = a.idAddress")
   Page<CustomerDTO> findAllCustomers(Pageable page);
}
