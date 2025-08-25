package org.dci.aimealplanner.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.User;
import org.dci.aimealplanner.models.Role;
import org.dci.aimealplanner.models.UserType;
import org.dci.aimealplanner.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/signup")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model) {
        List<String> errors = new ArrayList<>();

        if (userService.emailAlreadyExists(user.getEmail())) {
            errors.add("Email is already taken.");
        }

        if (!userService.ifPasswordMatchesPattern(user.getPassword())) {
            errors.add("Password must be at least 6 characters and contain uppercase," +
                    " lowercase, number and special character");
        }

        if (!errors.isEmpty() || bindingResult.hasErrors()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setUserType(UserType.LOCAL);
        user.setEmailVerified(true);
        userService.create(user);

        return "redirect:/home/index";


    }
}
