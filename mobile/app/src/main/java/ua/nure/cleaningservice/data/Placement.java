package ua.nure.cleaningservice.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Placement {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("placementType")
    @Expose
    private String placementType;
    @SerializedName("floor")
    @Expose
    private Integer floor;
    @SerializedName("windowsCount")
    @Expose
    private Integer windowsCount;
    @SerializedName("area")
    @Expose
    private Float area;
    @SerializedName("lastCleaning")
    @Expose
    private String lastCleaning;
    @SerializedName("smartDevice")
    @Expose
    private SmartDevice mSmartDevice;

    public Placement(Integer id, String placementType, Integer floor, Integer windowsCount, Float area, String lastCleaning, SmartDevice smartDevice) {
        this.id = id;
        this.placementType = placementType;
        this.floor = floor;
        this.windowsCount = windowsCount;
        this.area = area;
        this.lastCleaning = lastCleaning;
        mSmartDevice = smartDevice;
    }

    public Placement() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlacementType() {
        return placementType;
    }

    public void setPlacementType(String placementType) {
        this.placementType = placementType;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getWindowsCount() {
        return windowsCount;
    }

    public void setWindowsCount(Integer windowsCount) {
        this.windowsCount = windowsCount;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public String getLastCleaning() {
        return lastCleaning;
    }

    public void setLastCleaning(String lastCleaning) {
        this.lastCleaning = lastCleaning;
    }

    public SmartDevice getSmartDevice() {
        return mSmartDevice;
    }

    public void setSmartDevice(SmartDevice smartDevice) {
        this.mSmartDevice = smartDevice;
    }

}
