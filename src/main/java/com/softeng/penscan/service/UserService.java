package com.softeng.penscan.service;

import com.softeng.penscan.model.User;
import com.softeng.penscan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false;
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);

        return true;
    }

    public boolean loginUser(String username, String password) {
        User user = (User) userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User getUserByUsername(String username) {
        return (User) userRepository.findByUsername(username);
    }

    public String getUserTypeByUsername(String username) {
        User user = (User) userRepository.findByUsername(username);
        if (user != null) {
            return user.getUserType();
        } else {
            return null;
        }
    }
}
