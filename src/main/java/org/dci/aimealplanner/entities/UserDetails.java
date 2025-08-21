package org.dci.aimealplanner.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "user_dietary_preferences",
            joinColumns = @JoinColumn(name = "user_details_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_id")
    )
    private Set<DietaryPreference> dietaryPreferences = new HashSet<>();
}
