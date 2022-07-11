package com.example.quiz.service;

import com.example.quiz.model.Role;
import com.example.quiz.model.User;
import com.example.quiz.repository.RoleRepository;
import com.example.quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

        if (userRepository.findAll().isEmpty()) {
            User defaultAdmin = new User();
            defaultAdmin.setUserName("admin");
            defaultAdmin.setPassword(bCryptPasswordEncoder.encode("admin"));
            defaultAdmin.setEmail("admin@admin.com");
            defaultAdmin.setName("Admin");
            defaultAdmin.setLastName("Admin");
            roleRepository.save(new Role(1, "ADMIN"));
            roleRepository.save(new Role(2, "USER"));
            Role role = roleRepository.findByRole("ADMIN");
            defaultAdmin.setRoles(new HashSet<>(List.of(role)));
            defaultAdmin.setActive(true);
            userRepository.save(defaultAdmin);
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

}
