package edu.pucmm.practica13.services;

import edu.pucmm.practica13.controller.DeviceController;
import edu.pucmm.practica13.data.Device;
import edu.pucmm.practica13.data.User;
import edu.pucmm.practica13.exception.BadRequestException;
import edu.pucmm.practica13.payload.Auth.SignUpRequest;
import edu.pucmm.practica13.payload.Device.DeviceRequest;
import edu.pucmm.practica13.repository.DeviceRepository;
import edu.pucmm.practica13.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    public Device find(Long id) {return  deviceRepository.findById(id).orElseThrow(() -> new BadRequestException("Not found")); }
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    public Collection<String> getAllDevicesIdAndNames(){
        return getAllDevices().stream().map(e -> {return e.getId() + " - " + e.getName();}).collect(Collectors.toList());
    }
    public void deleteDevice(Device device){
        deviceRepository.delete(device);
    }

    public Device store(Device device) {
        return  deviceRepository.save(device);
    }
}
