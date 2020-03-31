package edu.pucmm.practica13;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pucmm.practica13.services.DeviceMessageServices;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ConsumerRabbit {

    @Autowired
    DeviceMessageServices deviceMessageServices;

    @RabbitListener(queues = "dev-temp")
    public void receivedMessage(Message message) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = mapper.readTree(new String(message.getBody()));

        String device = "DEV-01";

        deviceMessageServices.saveDevice(device, jsonNode);
    }
}
