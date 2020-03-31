package edu.pucmm.practica13.payload.DeviceMessage;

import edu.pucmm.practica13.payload.Device.DeviceRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class DeviceMessageRequest {
    private Long id;
    @NotNull
    private Timestamp date;
    @NotNull
    private Double temperature;
    @NotNull
    private Double humidity;
    @NotNull
    private Long device;

    public Long getId() {
        return id;
    }

    public DeviceMessageRequest setId(Long id) {
        this.id = id;

        return this;
    }

    public Timestamp getDate() {
        return date;
    }

    public DeviceMessageRequest setDate(Timestamp date) {
        this.date = date;

        return this;
    }

    public Double getTemperature() {
        return temperature;
    }

    public DeviceMessageRequest setTemperature(Double temperature) {
        this.temperature = temperature;

        return this;
    }

    public Double getHumidity() {
        return humidity;
    }

    public DeviceMessageRequest setHumidity(Double humidity) {
        this.humidity = humidity;

        return this;
    }

    public Long getDevice() {
        return device;
    }

    public DeviceMessageRequest setDevice(Long device) {
        this.device = device;

        return this;
    }
}
