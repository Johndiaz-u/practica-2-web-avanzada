package edu.pucmm.practica13.payload.Device;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class DeviceRequest {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String name;
    @NotNull
    private int alarmTime;
    private Set<Long> messages;

    public Long getId() {
        return id;
    }

    public DeviceRequest setId(Long id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public DeviceRequest setName(String name) {
        this.name = name;

        return this;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public DeviceRequest setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;

        return this;
    }

    public Set<Long> getMessages() {
        return messages;
    }

    public DeviceRequest setMessages(Set<Long> messages) {
        this.messages = messages;

        return this;
    }
}
