package com.eai.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eai.user.entities.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

}
