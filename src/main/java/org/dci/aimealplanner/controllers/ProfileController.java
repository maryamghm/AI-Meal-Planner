package org.dci.aimealplanner.controllers;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.users.UserInformation;
import org.dci.aimealplanner.repositories.users.UserInformationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserInformationRepository userInformationRepository;

    @GetMapping
    public String showProfile(Model model) {
        model.addAttribute("userInformation", new UserInformation());
        return "profile/user-profile";
    }

    @PostMapping
    public String saveProfile(UserInformation userInformation) {
        userInformationRepository.save(userInformation);
        return "redirect:/dashboard";
    }

    @DeleteMapping("/{id}")
    public String deleteProfile(@PathVariable Long id) {
        userInformationRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    @GetMapping("/skip")
    public String skipProfile() {
        return "redirect:/dashboard";
    }
}