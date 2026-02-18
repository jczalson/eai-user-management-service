package com.eai.user.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.eai.user.dto.EventTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "EVENT")
@Getter
@Setter
@RequiredArgsConstructor
public class EventEntity implements Serializable {

  private static final long serializableId = 1L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idEvent;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  private EventTypeEnum type;

  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
  private List<UserEventEntity> userEvents = new ArrayList<>();
}
