package ua.nure.dto;

import java.util.Date;

public interface ContractInfoDto {

    public Long getId();

    public Date getDate();

    public Double getPrice();

    public String getServiceName();

    public String getCleaningProviderName();

    public String getPlacementOwnerName();

    public Long getPlacementId();

    public Long getProviderServiceId();
}
