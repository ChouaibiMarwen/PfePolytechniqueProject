package com.camelsoft.rayaserver.Models.Auth;

import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.Event;
import com.camelsoft.rayaserver.Models.User.users;
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
    @OneToMany(mappedBy = "role")
    private Set<users> user = new HashSet<>();

    public Role() {
    }


}
