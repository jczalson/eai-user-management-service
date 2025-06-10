package com.eai.user.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "APP_USER")
@Getter
@Setter
@RequiredArgsConstructor
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER")
    private long idUser;

    @Column(name = "EMAIL")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "PWD")
    private String password;

    // @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatusEnum userStatusEnum;
    
    // @ManyToMany
    // @JoinTable(name = "APP_USER_ROLE",
    // joinColumns = @JoinColumn(name = "ID_USER"),
    // inverseJoinColumns = @JoinColumn(name = "ID_ROLE"))
    @OneToMany(mappedBy = "appUser")
    private List<AppUserRole> userRoleList = new ArrayList<>();
}
