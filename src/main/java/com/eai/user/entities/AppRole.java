package com.eai.user.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "APP_ROLE")
@Setter
@Getter
@RequiredArgsConstructor
public class AppRole implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROLE")
    private long idRole;

    @Column(name = "RL_NM")
    private String rlNm;

    @OneToMany(mappedBy = "appRole")
    private List<AppUserRole> userRoleList = new ArrayList<>();
}
