package ua.nure.cleaningservice.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmartDevice {
    @SerializedName("airQuality")
    @Expose
    private Float airQuality;
    @SerializedName("humidity")
    @Expose
    private Float humidity;
    @SerializedName("adjustmentFactor")
    @Expose
    private Float adjustmentFactor;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("temperature")
    @Expose
    private Float temperature;
    @SerializedName("dirtinessFactor")
    @Expose
    private Float dirtinessFactor;

    public SmartDevice(Float airQuality, Float humidity, Float adjustmentFactor,
            String priority) {
        this.airQuality = airQuality;
        this.humidity = humidity;
        this.adjustmentFactor = adjustmentFactor;
        this.priority = priority;
    }

    public SmartDevice(Float airQuality, Float humidity, Float adjustmentFactor,
            String priority, Float temperature, Float dirtinessFactor) {
        this(airQuality, humidity, adjustmentFactor, priority);
        this.temperature = temperature;
        this.dirtinessFactor = dirtinessFactor;
    }

    public Float getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(Float airQuality) {
        this.airQuality = airQuality;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getAdjustmentFactor() {
        return adjustmentFactor;
    }

    public void setAdjustmentFactor(Float adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getDirtinessFactor() {
        return dirtinessFactor;
    }

    public void setDirtinessFactor(Float dirtinessFactor) {
        this.dirtinessFactor = dirtinessFactor;
    }
}
