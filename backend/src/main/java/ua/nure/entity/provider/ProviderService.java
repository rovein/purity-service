package ua.nure.entity.provider;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.nure.entity.user.Contract;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = {"cleaningProvider"})
@EqualsAndHashCode(exclude = {"cleaningProvider"})
@Accessors(chain = true)
public class ProviderService {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "provider_service_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "min_area")
    private Integer minArea;

    @Column(name = "max_area")
    private Integer maxArea;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "price_per_meter")
    private Double pricePerMeter;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleaning_provider_id")
    protected CleaningProvider cleaningProvider;

    @JsonIgnore
    @OneToMany(mappedBy = "providerService", fetch = FetchType.LAZY)
    Set<Contract> contracts;

}
