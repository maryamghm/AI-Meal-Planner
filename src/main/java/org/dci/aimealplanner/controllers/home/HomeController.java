package org.dci.aimealplanner.controllers.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/index")
    public String index() {
        return "home/index";
    }

    @GetMapping("/account")
    public String account() {
        return "home/user_dashboard";
    }
}
