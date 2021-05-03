package ua.nure.cleaningservice.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("minArea")
    @Expose
    private Integer minArea;
    @SerializedName("maxArea")
    @Expose
    private Integer maxArea;
    @SerializedName("placementType")
    @Expose
    private String placementType;
    @SerializedName("pricePerMeter")
    @Expose
    private Float pricePerMeter;

    public Service() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinArea() {
        return minArea;
    }

    public void setMinArea(Integer minArea) {
        this.minArea = minArea;
    }

    public Integer getMaxArea() {
        return maxArea;
    }

    public void setMaxArea(Integer maxArea) {
        this.maxArea = maxArea;
    }

    public String getPlacementType() {
        return placementType;
    }

    public void setPlacementType(String placementType) {
        this.placementType = placementType;
    }

    public Float getPricePerMeter() {
        return pricePerMeter;
    }

    public void setPricePerMeter(Float pricePerMeter) {
        this.pricePerMeter = pricePerMeter;
    }

    public Service(Integer id, String name, String description, Integer minArea,
            Integer maxArea, String placementType, Float pricePerMeter) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.placementType = placementType;
        this.pricePerMeter = pricePerMeter;
    }

}