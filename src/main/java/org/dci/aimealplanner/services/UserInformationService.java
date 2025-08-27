package org.dci.aimealplanner.services;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.repositories.users.UserInformationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInformationService {
    private final UserInformationRepository userInformationRepository;
}
