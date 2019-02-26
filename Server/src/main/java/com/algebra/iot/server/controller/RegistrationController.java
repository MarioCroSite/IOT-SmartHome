package com.algebra.iot.server.controller;

import com.algebra.iot.server.dao.model.Gateway;
import com.algebra.iot.server.dao.model.User;
import com.algebra.iot.server.dao.model.form.RegistrationForm;
import com.algebra.iot.server.dao.repo.GatewayRepository;
import com.algebra.iot.server.dao.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author matij
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    GatewayRepository repository;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "register";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        Gateway gateway=repository.findFirstByOrderByIdDesc();
        Set<Gateway> gateways=new HashSet<>();
        gateways.add(gateway);
        User user=form.toUser(passwordEncoder);
        userRepository.saveUserWithAuthorities(user);
        return "redirect:/login";
    }
}