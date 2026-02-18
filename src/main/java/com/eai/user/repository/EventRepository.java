package com.eai.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eai.user.dto.EventTypeEnum;
import com.eai.user.entities.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity,Long> {

  public Optional<EventEntity> findEventEntityByType(EventTypeEnum type);
}
