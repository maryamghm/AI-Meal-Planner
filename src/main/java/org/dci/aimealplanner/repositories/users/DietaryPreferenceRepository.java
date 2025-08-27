package org.dci.aimealplanner.repositories.users;

import org.dci.aimealplanner.entities.users.DietaryPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietaryPreferenceRepository extends JpaRepository<DietaryPreference, Long> {
}
