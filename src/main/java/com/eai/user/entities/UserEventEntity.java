package com.eai.user.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER_EVENT")
@Getter
@Setter
@RequiredArgsConstructor
public class UserEventEntity implements Serializable{

  private static final long serializableId = 1L;

  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idUserEvent;

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "device")
  private String device;

  @Column(name = "crt_at")
  private Timestamp createAt;

  @ManyToOne
  @JoinColumn(name = "USER_ID")
  private AppUser user;

  @ManyToOne
  @JoinColumn(name = "EVENT_ID")
  private EventEntity event;

}
