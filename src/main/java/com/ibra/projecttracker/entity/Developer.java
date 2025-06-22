package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.DevSkills;
import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "developers")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("DEVELOPER")

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Developer extends User {
    @Enumerated(EnumType.STRING)
    private DevSkills skill;

    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskAssignment> taskAssignments;

    public Developer(String firstName, String lastName, String password, String email, String phoneNumber,
                     DevSkills skill) {
        super(password, email, phoneNumber, firstName, lastName, UserRole.DEVELOPER);
        this.skill = skill;
    }
}