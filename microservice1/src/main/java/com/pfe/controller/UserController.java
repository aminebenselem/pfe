package com.pfe.controller;


import com.pfe.Exceptions.InvalidCredentialsException;
import com.pfe.config.TokenGeneration;
import com.pfe.entity.User;
import com.pfe.repository.RoleRepository;
import com.pfe.repository.UserRepository;
import com.pfe.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private final RoleRepository roleRepository;
    private final PasswordEncoder pe;
    private final TokenGeneration tokenGeneration;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserController(
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            TokenGeneration tokenGeneration,
            UserService userService,
            AuthenticationManager authenticationManager,
            UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.pe = passwordEncoder;
        this.tokenGeneration = tokenGeneration;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }
    @GetMapping("/user/{username}")
    public User getUserByUsername(@PathVariable("username") String username){

        return userService.getUser(username);
    }
    @GetMapping("/users")
    public List<User> getUsers(){

        return userRepository.findAll();
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody User user) throws Exception {
        Optional<User> connectingUser = userService.getUserByUsernameOrEmail(user.getUsername(), user.getUsername());

        if (connectingUser.isEmpty()) {
            throw new UsernameNotFoundException("User with this username or email does not exist.");
        }

        UserDetails userDetails = userService.loadUserByUsername(connectingUser.get().getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), user.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }

        String token = tokenGeneration.generateToken(userDetails);

        User authenticatedUser = connectingUser.get();

        // Create a response map
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", authenticatedUser.getUsername());
        response.put("role", authenticatedUser.getRole().getName());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody User newUser) throws Exception {
        // Validate email format

        if (userService.getUserByUsernameOrEmail(newUser.getUsername(), newUser.getEmail()).isPresent()) {
            throw new InvalidCredentialsException("User with this username or email already exists.");
        }

        User user = new User();

        user.setUsername(newUser.getUsername());
        user.setRole(roleRepository.findById(2L).get());
        user.setEmail(newUser.getEmail());

        user.setPassword(pe.encode(newUser.getPassword()));

        userService.addUser(user);
        Map<String, Object> response = new HashMap<>();

        response.put("status", "User registered successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);    }


}
