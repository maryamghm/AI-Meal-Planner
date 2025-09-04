package org.dci.aimealplanner.services.users;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.users.User;
import org.dci.aimealplanner.exceptions.EmailAlreadyTaken;
import org.dci.aimealplanner.exceptions.PasswordInvalid;
import org.dci.aimealplanner.exceptions.VerificationTokenInvalid;
import org.dci.aimealplanner.models.Role;
import org.dci.aimealplanner.models.UserType;
import org.dci.aimealplanner.repositories.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";

    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String rawEmail) throws UsernameNotFoundException {
        final String email = normalize(rawEmail);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found."));

        if (user.getRole() == null) {
            throw new SecurityException("User " + user.getId() + " has no role assigned");
        }

        boolean enabled = user.isEmailVerified() && !user.isDeleted();

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

    public void checkEmailAvailability(String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyTaken(String.format("Email: %s is already taken.", email));
        }
    }

    public void checkPasswordValidity(String password) {
        if (!ifPasswordMatchesPattern(password)) {
            throw new PasswordInvalid("Password must be at least 6 characters and contain uppercase," +
                    " lowercase, number and special character");
        }
    }

    public boolean ifPasswordMatchesPattern(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setUserType(UserType.LOCAL);
        user.setEmailVerified(false);
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User findByVerificationToken(String token) {
        return userRepository.findByVerificationToken(token)
                .orElseThrow(() ->
                        new VerificationTokenInvalid("User with verification token %s not found.".
                                formatted(token))
                );
    }

    public void sendVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(),token);
    }

    public void verifyToken(String token) {
        User user = findByVerificationToken(token);
        user.setEmailVerified(true);
        user.setVerificationToken(null);

        update(user);
    }

    public User findByEmail(String email) {
       return userRepository.findByEmail(email).orElseThrow(() -> new EmailAlreadyTaken(email));
    }

    public boolean emailIsNotTaken(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public boolean isAdminExist() {
        return userRepository.findByRole(Role.ADMIN).isPresent();
    }

}
