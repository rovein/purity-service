package ua.nure.entity.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.nure.entity.user.Contract;
import ua.nure.entity.user.SmartDevice;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = {"placementOwner"})
@EqualsAndHashCode(exclude = {"placementOwner"})
@Accessors(chain = true)
public class Placement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "placement_id")
    private Long id;

    @Column(name = "placement_type")
    private String placementType;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "windows_count")
    private Integer windowsCount;

    @Column(name = "area")
    private Double area;

    @Column(name = "last_cleaning")
    private Date lastCleaning;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placement_owner_id")
    protected PlacementOwner placementOwner;

    @OneToOne(mappedBy = "placement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private SmartDevice smartDevice;

    @JsonIgnore
    @OneToMany(mappedBy = "placement", fetch = FetchType.LAZY)
    Set<Contract> contracts;

}
