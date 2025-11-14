package com.aplm.gdois.anunciosloc.anunciosloc.controller;


import com.aplm.gdois.anunciosloc.anunciosloc.entity.User;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")  // Permite Android
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}