package org.dci.aimealplanner.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "dietary_preferences")
public class DietaryPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
