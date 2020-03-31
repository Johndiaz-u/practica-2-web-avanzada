package edu.pucmm.practica13.payload.Device;

import edu.pucmm.practica13.data.DeviceMessage;
import edu.pucmm.practica13.payload.DeviceMessage.DeviceMessageResponse;

import java.util.HashSet;
import java.util.Set;

public class DeviceResponse {
    private Long id;
    private String name;
    private int alarmTime;
    private Set<DeviceMessageResponse> messages;

    public Long getId() {
        return id;
    }

    public DeviceResponse setId(Long id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public DeviceResponse setName(String name) {
        this.name = name;

        return this;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public DeviceResponse setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;

        return this;
    }

    public Set<DeviceMessageResponse> getMessages() {
        return messages;
    }

    public DeviceResponse setMessages(Set<DeviceMessageResponse> messages) {
        this.messages = messages;

        return this;
    }

    public DeviceResponse putMessages(Set<DeviceMessage> messages) {
        Set<DeviceMessageResponse> messagesResponse = new HashSet<>();

        messages.forEach(e -> {
                    messagesResponse.add(new DeviceMessageResponse()
                            .setId(e.getId())
                            .setDate(e.getDate())
                            .setTemperature(e.getTemperature())
                            .setHumidity(e.getHumidity())
                    );
        });

        return setMessages(messagesResponse);
    }
}
