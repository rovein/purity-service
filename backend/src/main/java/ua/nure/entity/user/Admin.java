package ua.nure.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ToString(callSuper = true)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "admin_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

}
