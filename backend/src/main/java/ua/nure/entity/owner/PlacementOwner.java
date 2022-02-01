package ua.nure.entity.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.nure.entity.user.Address;
import ua.nure.entity.user.User;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlacementOwner extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "placement_owner_id")
    private Long id;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "creation_date")
    private Date creationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "address_id")
    private Address address;

    @OneToMany(mappedBy = "placementOwner", fetch = FetchType.EAGER)
    Set<Placement> placements;

    @Override
    public String toString() {
        return "CustomerCompany{" +
                "id=" + id +
                ", \nname='" + name + '\'' +
                ", \ncreationDate=" + creationDate +
                ", \nemail='" + email + '\'' +
                ", \npassword='" + password + "'}";
    }
}
