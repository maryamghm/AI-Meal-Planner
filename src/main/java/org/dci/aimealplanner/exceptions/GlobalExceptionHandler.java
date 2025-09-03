package org.dci.aimealplanner.exceptions;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTaken.class)
    public String handleEmailAlreadyTaken(EmailAlreadyTaken ex, Model model){

        model.addAttribute("errorMassage", ex.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(PasswordInvalid.class)
    public String handlePasswordInvalid(PasswordInvalid ex, Model model ){

        model.addAttribute("errorMassage", ex.getMessage());
        return "errors/404";

    }

    @ExceptionHandler(VerificationTokenInvalid.class)
    public String handleVerificationTokenInvalid(VerificationTokenInvalid ex, Model model ){

        model.addAttribute("errorMassage", ex.getMessage());
        return "errors/404";

    }


}
