package com.camelsoft.rayaserver.Models.Auth;

import com.camelsoft.rayaserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "role_id" )
    private Long id;
    @Column(name = "role")
    private String role;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<users> user = new HashSet<>();
    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<users> getUser() {
        return user;
    }

    public void setUser(Set<users> user) {
        this.user = user;
    }
}
