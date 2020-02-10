package org.dabs.back.entity;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@EqualsAndHashCode
public class Role implements GrantedAuthority, Serializable {

	private static final long serialVersionUID = -8678311268738613111L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
