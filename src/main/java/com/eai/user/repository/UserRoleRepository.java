package com.eai.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eai.user.entities.AppUserRole;
import com.eai.user.entities.AppUserRoleKey;

public interface UserRoleRepository extends JpaRepository<AppUserRole,AppUserRoleKey>{

}
