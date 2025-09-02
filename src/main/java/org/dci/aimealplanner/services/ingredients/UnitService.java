package org.dci.aimealplanner.services.ingredients;

import lombok.RequiredArgsConstructor;
import org.dci.aimealplanner.entities.ingredients.Unit;
import org.dci.aimealplanner.repositories.ingredients.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;

    public Unit findByCode(String code) {
        return unitRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Unit with code " + code + " not found"));
    }

    public Unit findByDisplayName(String displayName) {
        return unitRepository.findByDisplayName(displayName).orElseThrow(() -> new RuntimeException("Unit with display name " + displayName + " not found"));
    }

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }
}
