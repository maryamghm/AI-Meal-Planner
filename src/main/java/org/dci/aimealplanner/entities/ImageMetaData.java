package org.dci.aimealplanner.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dci.aimealplanner.entities.recipes.Recipe;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageMetaData {
    @Id
    @NotBlank(message = "Public ID cannot be blank")
    private String publicId;

    @NotBlank(message = "Image URL cannot be blank")
    @Column(nullable = false)
    private String imageUrl;
}
