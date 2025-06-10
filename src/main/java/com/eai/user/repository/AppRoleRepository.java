package com.eai.user.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eai.user.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole,Long>{

    Optional<AppRole> findByRoleName(String roleName);

    

}
