package edu.pucmm.practica13.data;

import edu.pucmm.practica13.exception.BadRequestException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Device implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "alarm_time", nullable = false)
    private int alarmTime;
    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
    private Set<DeviceMessage> messages;

    public Long getId() {
        return id;
    }

    public Device setId(Long id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public Device setName(String name) {
        this.name = name;

        return this;
    }

    public int getAlarmTime() {
        return alarmTime;
    }

    public Device setAlarmTime(int alarmTime) {
        this.alarmTime = alarmTime;

        return this;
    }

    public Set<DeviceMessage> getMessages() {
        return messages;
    }

    public DeviceMessage getLastMessage() {
        return this.messages.stream().reduce((first, second) -> second).orElse(null);
    }

    public Device setMessages(Set<DeviceMessage> messages) {
        this.messages = messages;

        return this;
    }

    public Device addMessage(DeviceMessage message) {
        if (this.messages == null) {
            this.messages = new HashSet<>();
        }

        this.messages.add(message);

        return this;
    }
}