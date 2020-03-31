package edu.pucmm.practica13.payload.DeviceMessage;

import edu.pucmm.practica13.payload.Device.DeviceResponse;
import java.sql.Timestamp;

public class DeviceMessageResponse {
    private Long id;
    private Timestamp date;
    private Double temperature;
    private Double humidity;
    private DeviceResponse device;

    public Long getId() {
        return id;
    }

    public DeviceMessageResponse setId(Long id) {
        this.id = id;

        return this;
    }

    public Timestamp getDate() {
        return date;
    }

    public DeviceMessageResponse setDate(Timestamp date) {
        this.date = date;

        return this;
    }

    public Double getTemperature() {
        return temperature;
    }

    public DeviceMessageResponse setTemperature(Double temperature) {
        this.temperature = temperature;

        return this;
    }

    public Double getHumidity() {
        return humidity;
    }

    public DeviceMessageResponse setHumidity(Double humidity) {
        this.humidity = humidity;

        return this;
    }

    public DeviceResponse getDevice() {
        return device;
    }

    public DeviceMessageResponse setDevice(DeviceResponse device) {
        this.device = device;

        return this;
    }
}
