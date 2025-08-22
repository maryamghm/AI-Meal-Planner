package org.dci.aimealplanner.repositories;

import org.dci.aimealplanner.entities.User;
import org.dci.aimealplanner.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRole(Role role);
    Optional<User> findByVerificationToken(String verificationToken);
}
