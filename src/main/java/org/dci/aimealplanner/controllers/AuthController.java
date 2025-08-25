package org.dci.aimealplanner.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.User;
import org.dci.aimealplanner.models.Role;
import org.dci.aimealplanner.models.UserType;
import org.dci.aimealplanner.services.EmailService;
import org.dci.aimealplanner.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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
                    " lowercase, number and special character.");
        }

        if (!errors.isEmpty() || bindingResult.hasErrors()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "auth/register";
        }

        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        emailService.sendVerificationEmail(user.getEmail(),token);
        user.setEmailVerified(false);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setUserType(UserType.LOCAL);

        userService.create(user);

        return "redirect:/auth/login?registered";

    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token, Model model) {
        if (userService.userExistWithVerificationToken(token)) {
            User user = userService.findByVerificationToken(token);

            user.setEmailVerified(true);
            user.setVerificationToken(null);

            userService.update(user);

            return "redirect:/auth/login?verified";
        } else {
            return "redirect:/auth/login?invalidToken";
        }
    }
}
