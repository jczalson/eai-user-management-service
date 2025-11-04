package com.eai.user.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TWOFACTORVERIFICATIONS")
@Setter
@Getter
@RequiredArgsConstructor
public class TwoFactorVerificationsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "CODE",unique = true)
    private String code;

    @Column(name = "EXPIRY_DATE")
    private Timestamp expiryDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_USER",
    referencedColumnName = "ID_USER")
    private AppUser user;
    
}
