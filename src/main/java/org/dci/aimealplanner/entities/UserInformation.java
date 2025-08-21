package org.dci.aimealplanner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dci.aimealplanner.models.ActivityLevel;
import org.dci.aimealplanner.models.Goal;
import org.dci.aimealplanner.models.Sex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_infos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Sex gender;

    private Integer heightCm;
    private BigDecimal weightKg;

    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    private Goal goal;
    private Integer mealsPerDay;


    private Integer targetKcalPerDay;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "user_dietary_preferences",
            joinColumns = @JoinColumn(name = "user_info_id"),
            inverseJoinColumns = @JoinColumn(name = "preference_id")
    )
    private Set<DietaryPreference> dietaryPreferences = new HashSet<>();
}
