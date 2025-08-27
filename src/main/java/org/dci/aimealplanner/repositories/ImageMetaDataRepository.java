package org.dci.aimealplanner.repositories;

import org.dci.aimealplanner.entities.ImageMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetaDataRepository extends JpaRepository<ImageMetaData, Long> {
}
