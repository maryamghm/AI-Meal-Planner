package org.dci.aimealplanner.controllers.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.users.User;
import org.dci.aimealplanner.exceptions.EmailAlreadyTaken;
import org.dci.aimealplanner.exceptions.PasswordInvalid;
import org.dci.aimealplanner.exceptions.VerificationTokenInvalid;
import org.dci.aimealplanner.services.users.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

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

        try {
            userService.checkEmailAvailability(user.getEmail());
            userService.checkPasswordValidity(user.getPassword());
        } catch (EmailAlreadyTaken | PasswordInvalid ex) {
            errors.add(ex.getMessage());
        }

        if (!errors.isEmpty() || bindingResult.hasErrors()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", user);
            return "auth/register";
        }

        User addedUser = userService.create(user);

        userService.sendVerificationToken(addedUser);

        return "redirect:/auth/login?registered";

    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token, Model model) {
       try {
           userService.verifyToken(token);
           return "redirect:/auth/login?verified";
       } catch (VerificationTokenInvalid ex){
            return "redirect:/auth/login?invalidToken";
        }
    }
}
