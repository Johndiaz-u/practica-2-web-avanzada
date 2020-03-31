package edu.pucmm.practica13.services;


import com.fasterxml.jackson.databind.JsonNode;
import edu.pucmm.practica13.data.Device;
import edu.pucmm.practica13.data.DeviceMessage;
import edu.pucmm.practica13.repository.DeviceMessageRepository;
import edu.pucmm.practica13.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static edu.pucmm.practica13.Practica13Application.LOG;

@Service
public class DeviceMessageServices {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceMessageRepository deviceMessageRepository;

    public void saveDevice(String device, JsonNode jsonNode) {

        System.out.println(jsonNode.toString());

        Device deviceObject = getDevice(device);

        JsonNode tempNode = jsonNode.get("temperatura");
        double temperature = tempNode.asDouble();

        System.out.println(temperature);

        JsonNode humedityNode = jsonNode.get("humedad");
        double humidity = humedityNode.asDouble();

        System.out.println(humidity);

        DeviceMessage deviceMessage = new DeviceMessage()
                .setDevice(deviceObject)
                .setHumidity(humidity)
                .setTemperature(temperature)
                .setDate(new Timestamp(System.currentTimeMillis()));

        deviceMessageRepository.save(deviceMessage);
        System.out.println("Saved successfully");
    }

    public List<DeviceMessage> getAllDeviceMessages() {
        return deviceMessageRepository.findAll();
    }
    public List<DeviceMessage> getAllDeviceMessagesByDevice(Long id) {
        return deviceMessageRepository.findAllByDeviceId(id);
    }

    public List<DeviceMessage> getAllDeviceMessagesPaginated(Long deviceId, LocalDate start, LocalDate end, int offset, int limit){
        LOG.info("PAGINATION: DEVICE = " + deviceId + ", START: " + start.toString() + ", END = " + end.toString() + ", OFFSET = " + offset + ", LIMIT = " + limit);
        return deviceMessageRepository.findAllPaginated(deviceId, start, end, offset, limit);
    }

    public DeviceMessage getLastMessageById(Long id){
        try{
            return deviceMessageRepository.findLastByDeviceId(id).get(0);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    private Device getDevice(String name) {
        return deviceRepository.getByName(name).orElse(null);
    }

}
