package edu.pucmm.practica13.controller;

import edu.pucmm.practica13.data.Device;
import edu.pucmm.practica13.exception.AppException;
import edu.pucmm.practica13.exception.BadRequestException;
import edu.pucmm.practica13.payload.Device.DeviceRequest;
import edu.pucmm.practica13.payload.Device.DeviceResponse;
import edu.pucmm.practica13.payload.DeviceMessage.DeviceMessageResponse;
import edu.pucmm.practica13.payload.JsonResponse;
import edu.pucmm.practica13.repository.DeviceMessageRepository;
import edu.pucmm.practica13.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceMessageRepository deviceMessageRepository;

    @GetMapping
    public ResponseEntity<?> getAllDevices(HttpServletResponse response) {
        List<DeviceResponse> devices = deviceRepository.findAll().stream()
                .map(e -> {
                    return new DeviceResponse()
                            .setId(e.getId())
                            .setName(e.getName())
                            .setAlarmTime(e.getAlarmTime());
//                            .putMessages(e.getMessages());
                }).collect(Collectors.toList());

        return ResponseEntity.ok(new JsonResponse(devices, "Device and messages List", null, null, response.getStatus()));
    }

    @GetMapping
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDeviceById(@PathVariable Long id) {

        Device e = deviceRepository.findById(id).orElseThrow(() -> new BadRequestException("Not found"));

        DeviceResponse deviceResponse =  new DeviceResponse()
                .setId(e.getId())
                .setName(e.getName())
                .setAlarmTime(e.getAlarmTime())
                .putMessages(e.getMessages());

        return ResponseEntity.ok(new JsonResponse(deviceResponse, null, null, null, 200));
    }

    @PostMapping
    public ResponseEntity<?> PostDevice(@Valid @RequestBody DeviceRequest deviceRequest) {

        Device d = new Device()
                .setName(deviceRequest.getName())
                .setAlarmTime(deviceRequest.getAlarmTime());

        deviceRepository.save(d);

        DeviceResponse deviceResponse = new DeviceResponse()
                .setId(d.getId())
                .setName(d.getName())
                .setAlarmTime(d.getAlarmTime());

        return ResponseEntity.ok(new JsonResponse(deviceResponse, "", null, null, 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(HttpServletResponse response, @PathVariable Long id) {
        Device device = deviceRepository.findById(id).orElseThrow(() -> new AppException("Device not found"));

        deviceRepository.delete(device);

        return ResponseEntity.ok(new JsonResponse("Device deleted!", null, null, null, response.getStatus()));
    }
}
