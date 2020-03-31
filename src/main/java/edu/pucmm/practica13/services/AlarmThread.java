package edu.pucmm.practica13.services;

import edu.pucmm.practica13.data.Device;
import edu.pucmm.practica13.data.DeviceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class AlarmThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmThread.class);

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceMessageServices deviceMessageServices;

    @Autowired
    public JavaMailSender emailSender;

    @Override
    public void run() {
        try{
            Map<String, Long> lastMessagesIds = new HashMap<>();
        while (true) {
            Thread.sleep(1000);
            List<Device> devices = deviceService.getAllDevices();

            for (Device device: devices) {
                try{
                    int seconds = device.getAlarmTime();
                    Long lastMessageId = lastMessagesIds.get(device.getName());
                    Long newLastMessageId = deviceMessageServices.getLastMessageById(device.getId()).getId();
                    if(lastMessageId != null && newLastMessageId != null){
                        Timestamp newLastMessageDate =  deviceMessageServices.getLastMessageById(device.getId()).getDate();
                        if(new Timestamp(System.currentTimeMillis() - seconds * 1000).after(newLastMessageDate) && lastMessageId == newLastMessageId){
                            String emailMsg = "AN ALARM WAS ACTIVATED FOR YOUR DEVICE " + device.getName() + "\n\n";
                            emailMsg += "=== ALARM INFORMATION ===\n";
                            emailMsg += "LAST MESSAGE TIME: " + newLastMessageDate + "\n";
                            emailMsg += "DEVICE ALARM CHECK IS: " + seconds + "SECONDS\n";
                            emailMsg += "ALARM WAS TRIGGERED AT " + new Timestamp(System.currentTimeMillis() - seconds * 1000);
                            SimpleMailMessage message = new SimpleMailMessage();
                            message.setTo("johndiaz-u@hotmail.com");
                            message.setSubject("ALARM ACTIVATED FOR DEVICE " + device.getName());
                            message.setText(emailMsg);
                            emailSender.send(message);

                            LOGGER.error("################################ ALARM ACTIVATED FOR DEVICE: " + device.getName());
                        }
                        lastMessagesIds.put(device.getName(), newLastMessageId);
                    }else{
                        LOGGER.warn("################################ NULL");
                        lastMessagesIds.put(device.getName(), device.getId());
                    }

                }catch (NullPointerException e){
                    continue;
                }
            }
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
