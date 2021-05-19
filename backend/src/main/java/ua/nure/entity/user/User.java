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

    @Column(name = "is_locked")
    protected Boolean isLocked;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID")
    protected Role role;

    public User isLocked(boolean isLocked) {
        this.isLocked = isLocked;
        return this;
    }

    public Boolean isLocked() {
        return isLocked;
    }

}
