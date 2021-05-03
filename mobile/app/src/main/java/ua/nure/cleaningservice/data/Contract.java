package ua.nure.cleaningservice.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contract {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("price")
    @Expose
    private Float price;
    @SerializedName("placementId")
    @Expose
    private Integer placementId;
    @SerializedName("providerServiceId")
    @Expose
    private Integer providerServiceId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cleaningProviderName")
    @Expose
    private String cleaningProviderName;
    @SerializedName("placementOwnerName")
    @Expose
    private String placementOwnerName;

    @SerializedName("serviceName")
    @Expose
    private String serviceName;

    public Contract(String date, Float price, String serviceName,
            Integer placementId, Integer providerServiceId, Integer id,
            String cleaningProviderName, String placementOwnerName) {
        this.date = date;
        this.price = price;
        this.serviceName = serviceName;
        this.placementId = placementId;
        this.providerServiceId = providerServiceId;
        this.id = id;
        this.cleaningProviderName = cleaningProviderName;
        this.placementOwnerName = placementOwnerName;
    }

    public Contract() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getPlacementId() {
        return placementId;
    }

    public void setPlacementId(Integer placementId) {
        this.placementId = placementId;
    }

    public Integer getProviderServiceId() {
        return providerServiceId;
    }

    public void setProviderServiceId(Integer providerServiceId) {
        this.providerServiceId = providerServiceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCleaningProviderName() {
        return cleaningProviderName;
    }

    public void setCleaningProviderName(String cleaningProviderName) {
        this.cleaningProviderName = cleaningProviderName;
    }

    public String getPlacementOwnerName() {
        return placementOwnerName;
    }

    public void setPlacementOwnerName(String placementOwnerName) {
        this.placementOwnerName = placementOwnerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
