package org.dci.aimealplanner.repositories.users;

import org.dci.aimealplanner.entities.users.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {

}
