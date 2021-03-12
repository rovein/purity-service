package ua.nure.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.nure.entity.role.Role;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class User {

    @Column(name = "email", unique = true)
    protected String email;

    @Column(name = "password")
    protected String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID")
    protected Role role;

}
