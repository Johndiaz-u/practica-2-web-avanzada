package edu.pucmm.practica13.data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class DeviceMessage implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Timestamp date;
    @Column(nullable = false)
    private Double temperature;
    @Column(nullable = false)
    private Double humidity;
    @ManyToOne
    private Device device;

    public Long getId() {
        return id;
    }

    public DeviceMessage setId(Long id) {
        this.id = id;

        return this;
    }

    public Timestamp getDate() {
        return date;
    }

    public DeviceMessage setDate(Timestamp date) {
        this.date = date;

        return this;
    }

    public Double getTemperature() {
        return temperature;
    }

    public DeviceMessage setTemperature(Double temperature) {
        this.temperature = temperature;

        return this;
    }

    public Double getHumidity() {
        return humidity;
    }

    public DeviceMessage setHumidity(Double humidity) {
        this.humidity = humidity;

        return this;
    }

    public Device getDevice() {
        return device;
    }

    public DeviceMessage setDevice(Device device) {
        this.device = device;

        return this;
    }
}