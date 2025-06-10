package com.eai.user.entities;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Embeddable
public class AppUserRoleKey implements Serializable{

    private static final Long serializeId =1L;

    @Column(name = "ID_USER",insertable=false, updatable=false)
    private long idUser;

    @Column(name = "ID_ROLE",insertable=false, updatable=false)
    private long idRole;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (idUser ^ (idUser >>> 32));
        result = prime * result + (int) (idRole ^ (idRole >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppUserRoleKey other = (AppUserRoleKey) obj;
        if (idUser != other.idUser)
            return false;
        if (idRole != other.idRole)
            return false;
        return true;
    }

}
