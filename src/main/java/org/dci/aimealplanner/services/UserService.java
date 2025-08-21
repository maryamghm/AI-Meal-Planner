package org.dci.aimealplanner.services;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


}
