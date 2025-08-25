package org.dci.aimealplanner.services;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.User;
import org.dci.aimealplanner.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";

    @Override
    public UserDetails loadUserByUsername(String rawEmail) throws UsernameNotFoundException {
        final String email = rawEmail == null ? null : rawEmail.trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found."));

        boolean enabled = user.isEmailVerified() && !user.isDeleted();

        if (user.getRole() == null) {
            throw new SecurityException("User " + user.getId() + " has no role assigned");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(!enabled)
                .build();

    }

    public boolean emailAlreadyExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean ifPasswordMatchesPattern(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public boolean userExistWithVerificationToken(String verificationToken) {
        return userRepository.findByVerificationToken(verificationToken).isPresent();
    }

    public User findByVerificationToken(String token) {
        return userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException(
                        String.format("User with verification token %s not found.", token)));
    }

}
