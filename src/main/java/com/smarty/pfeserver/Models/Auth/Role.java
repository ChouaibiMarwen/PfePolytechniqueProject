package com.smarty.pfeserver.Models.Auth;

import com.smarty.pfeserver.Enum.User.RoleEnum;

import com.smarty.pfeserver.Models.User.users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Data
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "role_id" )
    private Long id;
    @Column(name = "role")
    private RoleEnum role;
    @JsonIgnore
    @OneToMany(mappedBy = "role" ,fetch = FetchType.EAGER)
    private Set<users> user = new HashSet<>();

    public Role() {
    }


}
