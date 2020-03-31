package edu.pucmm.practica13.controller;

import edu.pucmm.practica13.data.User;
import edu.pucmm.practica13.exception.AppException;
import edu.pucmm.practica13.payload.ApiResponse;
import edu.pucmm.practica13.payload.JsonResponse;
import edu.pucmm.practica13.payload.User.UserRequest;
import edu.pucmm.practica13.payload.User.UserResponse;
import edu.pucmm.practica13.repository.UserRepository;
import edu.pucmm.practica13.security.CurrentUser;
import edu.pucmm.practica13.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> userLogged(@CurrentUser UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.ok(new ApiResponse(false, "User not logged."));
        }

        return ResponseEntity.ok(new JsonResponse(userPrincipal, null, null, null, 200));
    }


    @GetMapping
    public ResponseEntity<?> getAllUsers(HttpServletResponse response) {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(e -> {
                    return new UserResponse()
                            .setId(e.getId())
                            .setEmail(e.getEmail())
                            .setName(e.getName())
                            .setUsername(e.getUsername());
                }).collect(Collectors.toList());

        return ResponseEntity.ok(new JsonResponse(users, "Users List", null, null, response.getStatus()));
    }

    @GetMapping
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(HttpServletResponse response, @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found"));
        UserResponse userResponse = new UserResponse()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setUsername(user.getUsername());
        return ResponseEntity.ok(new JsonResponse(userResponse, null, null, null, response.getStatus()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@Valid @RequestBody UserRequest userRequest, @PathVariable Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found"));

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }

        if (userRequest.getPassword() != null) {
            user.setPassword(userRequest.getPassword());
        }

        userRepository.saveAndFlush(user);

        UserResponse userResponse = new UserResponse()
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setName(user.getName());

        return ResponseEntity.ok(new JsonResponse(userResponse, "User updated", null, null, 201));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(HttpServletResponse response, @PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException("User not found"));

        userRepository.delete(user);

        return ResponseEntity.ok(new JsonResponse("User deleted!", null, null, null, response.getStatus()));
    }
}
