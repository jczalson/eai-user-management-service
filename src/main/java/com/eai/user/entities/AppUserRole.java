package com.eai.user.entities;

import java.io.Serializable;
import java.util.UUID;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name = "APP_USER_ROLE")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class AppUserRole implements Serializable {

    @SuppressWarnings("unused")
    private static final Long serializedId = 1L;

    @EmbeddedId
    private AppUserRoleKey appUserRoleKey;

    @Column(name = "ID_USER_ROLE")
    private long idUserRole;

    @ManyToOne
    @JoinColumn(name = "ID_USER")
    @MapsId("idUser")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "ID_ROLE")
    @MapsId("idRole")
    private AppRole appRole;

}
