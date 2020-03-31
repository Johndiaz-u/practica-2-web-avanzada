package edu.pucmm.practica13.services;

import edu.pucmm.practica13.data.User;
import edu.pucmm.practica13.exception.BadRequestException;
import edu.pucmm.practica13.payload.Auth.SignUpRequest;
import edu.pucmm.practica13.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User store(SignUpRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("E-mail address is already taken");
        }

        User user = new User()
                .setEmail(signUpRequest.getEmail())
                .setName(signUpRequest.getName())
                .setUsername(signUpRequest.getUsername())
                .setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        User result = userRepository.save(user);

        return result;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
