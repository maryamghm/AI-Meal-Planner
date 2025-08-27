package org.dci.aimealplanner.services;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.repositories.users.DietaryPreferenceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DietaryPreferenceService {
    private final DietaryPreferenceRepository dietaryPreferenceRepository;
}
