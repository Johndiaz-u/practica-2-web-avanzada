package edu.pucmm.practica13.controller;

import edu.pucmm.practica13.data.DeviceMessage;
import edu.pucmm.practica13.exception.BadRequestException;
import edu.pucmm.practica13.payload.Device.DeviceResponse;
import edu.pucmm.practica13.payload.DeviceMessage.DeviceMessageRequest;
import edu.pucmm.practica13.payload.DeviceMessage.DeviceMessageResponse;
import edu.pucmm.practica13.payload.JsonResponse;
import edu.pucmm.practica13.repository.DeviceMessageRepository;
import edu.pucmm.practica13.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/messages")
public class DeviceMessageController {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceMessageRepository deviceMessageRepository;

    @PostMapping
    public ResponseEntity<?> PostDeviceMessage(@Valid @RequestBody DeviceMessageRequest deviceMessageRequest) {

        DeviceMessage m = new DeviceMessage()
                .setDate(deviceMessageRequest.getDate())
                .setDevice(deviceRepository.findById(deviceMessageRequest.getDevice()).orElseThrow(() -> new BadRequestException("Not found")))
                .setHumidity(deviceMessageRequest.getHumidity())
                .setTemperature(deviceMessageRequest.getTemperature());

        deviceMessageRepository.save(m);

        DeviceMessageResponse deviceMessageResponse = new DeviceMessageResponse()
                .setDate(m.getDate())
                .setHumidity(m.getHumidity())
                .setTemperature(m.getTemperature())
                .setDevice(
                        new DeviceResponse()
                                .setId(m.getDevice().getId())
                                .setName(m.getDevice().getName())
                                .setAlarmTime(m.getDevice().getAlarmTime())
                );

        return ResponseEntity.ok(new JsonResponse(deviceMessageResponse, "", null, null, 200));
    }
}
