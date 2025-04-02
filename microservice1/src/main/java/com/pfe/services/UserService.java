package com.pfe.services;

import com.pfe.entity.User;
import com.pfe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepo;
    public User getUser(String username) {
        try {
            return userRepo.findByUsername(username).get();
        }catch(UsernameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + username, e);
        }
    }

    public User addUser(User user){
        return userRepo.save(user);
    }

    // Unban a user by username


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
    public Optional<User> getUserByUsernameOrEmail(String username, String email) {
        return userRepo.findByUsernameOrEmail(username, email);
    }

}
