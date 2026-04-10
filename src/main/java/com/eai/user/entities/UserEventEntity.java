package com.eai.user.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
  private Long id;

  @Column(name = "ip_address")
  private String ipAddress;

  @Column(name = "device")
  private String device;

  @Column(name = "crt_at")
  private Timestamp crtAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private AppUser user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "EVENT_ID")
  private EventEntity event;

}
