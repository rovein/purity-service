package ua.nure.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SelectBeforeUpdate;
import ua.nure.entity.owner.Placement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@SelectBeforeUpdate
@NoArgsConstructor
@Data
@Table(name = "smart_device")
@ToString(exclude = {"placement"})
@EqualsAndHashCode(exclude = {"placement"})
@Accessors(chain = true)
public class SmartDevice {

    @Id
    @Column(name = "placement_id")
    private Long id;

    @Column(name = "air_quality")
    private Double airQuality;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "adjustment_factor")
    private Double adjustmentFactor;

    @Column(name = "dirtiness_factor")
    private Double dirtinessFactor;

    @Column(name = "priority")
    private String priority;

    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "placement_id")
    private Placement placement;

}
