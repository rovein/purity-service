package ua.nure.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SelectBeforeUpdate;
import ua.nure.entity.provider.CleaningProvider;
import ua.nure.entity.owner.PlacementOwner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@SelectBeforeUpdate
@NoArgsConstructor
@Accessors(chain = true)
@ToString(exclude = {"cleaningProvider", "customerCompany"})
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private CleaningProvider cleaningProvider;

    @JsonIgnore
    @OneToOne(mappedBy = "address")
    private PlacementOwner placementOwner;

}
