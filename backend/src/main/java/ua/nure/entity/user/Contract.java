package ua.nure.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import ua.nure.entity.owner.Placement;
import ua.nure.entity.provider.ProviderService;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor
@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"providerService", "placement"})
@ToString(exclude = {"providerService", "placement"})
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_service_id")
    private ProviderService providerService;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "placement_id")
    @JsonIgnore
    private Placement placement;

}
