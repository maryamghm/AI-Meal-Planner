package org.dci.aimealplanner.controllers.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class AuthUtils {
    private AuthUtils() {}

    public static String getUserEmail(Authentication authentication) {
        String email;

        if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");
        } else {
            email = authentication.getName();
        }
        return email;
    }
}
