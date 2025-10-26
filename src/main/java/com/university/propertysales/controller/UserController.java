package com.university.propertysales.controller;

import com.university.propertysales.dto.LoginRequestDTO;
import com.university.propertysales.dto.UserCreateDTO;
import com.university.propertysales.dto.UserDTO;
import com.university.propertysales.dto.UserUpdateDTO;
import com.university.propertysales.entity.User;
import com.university.propertysales.mapper.UserMapper;
import com.university.propertysales.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        try {
            User user = userMapper.toEntity(userCreateDTO);
            User createdUser = userService.createUser(user);
            UserDTO userDTO = userMapper.toDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.toDTO(user.get());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        try {
            User.Role userRole = User.Role.valueOf(role.toUpperCase());
            List<User> users = userService.getUsersByRole(userRole);
            List<UserDTO> userDTOs = users.stream()
                    .map(userMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            userMapper.updateEntityFromDTO(userUpdateDTO, user);

            // Handle password separately
            if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
                user.setPassword(userUpdateDTO.getPassword());
            }

            User updatedUser = userService.updateUser(id, user);
            UserDTO userDTO = userMapper.toDTO(updatedUser);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            Optional<User> dbUser = userService.getUserByUsername(loginRequest.getUsername());

            if (dbUser.isPresent() && userService.validatePassword(loginRequest.getPassword(), dbUser.get().getPassword())) {
                System.out.println("Login successful for database user: " + dbUser.get().getUsername());
                UserDTO userDTO = userMapper.toDTO(dbUser.get());
                return ResponseEntity.ok(userDTO);
            }

            try {
                org.springframework.security.core.userdetails.UserDetails springUser =
                        userDetailsService.loadUserByUsername(loginRequest.getUsername());

                if (springUser != null && userService.validatePassword(loginRequest.getPassword(), springUser.getPassword())) {
                    // Create a User object for demo accounts
                    User demoUser = new User();
                    demoUser.setId(getDemoUserId(loginRequest.getUsername()));
                    demoUser.setUsername(springUser.getUsername());
                    demoUser.setEmail(getDemoUserEmail(loginRequest.getUsername()));
                    demoUser.setRole(getDemoUserRole(springUser.getAuthorities().iterator().next().getAuthority()));

                    System.out.println("Login successful for demo user: " + demoUser.getUsername() + " with role: " + demoUser.getRole());
                    UserDTO userDTO = userMapper.toDTO(demoUser);
                    return ResponseEntity.ok(userDTO);
                }
            } catch (UsernameNotFoundException e) {
                System.out.println("User not found: " + loginRequest.getUsername());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login error: " + e.getMessage());
        }
    }

    private Long getDemoUserId(String username) {
        switch (username) {
            case "admin": return 1000L;
            case "seller": return 1001L;
            case "buyer": return 1002L;
            case "agent": return 1003L;
            default: return 999L;
        }
    }

    private String getDemoUserEmail(String username) {
        return username + "@propertyhub.demo";
    }

    private User.Role getDemoUserRole(String springRole) {
        String role = springRole.replace("ROLE_", "");
        switch (role) {
            case "ADMIN": return User.Role.ADMIN;
            case "SELLER": return User.Role.SELLER;
            case "BUYER": return User.Role.BUYER;
            case "AGENT": return User.Role.AGENT;
            default: return User.Role.BUYER;
        }
    }
}
