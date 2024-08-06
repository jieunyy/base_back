package com.example.baseback.service;

import com.example.baseback.entity.User;
import com.example.baseback.dto.UserDto;
import com.example.baseback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setProvider("LOCAL");

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider);
    }
}
