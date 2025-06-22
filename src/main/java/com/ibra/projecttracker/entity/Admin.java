package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("ADMIN")

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Admin extends User {
    private String adminLevel;

    public Admin(String firstName, String lastName,String password, String email, String phoneNumber,
                 String adminLevel) {
        super(firstName, lastName, password, email, phoneNumber,  UserRole.ADMIN);
        this.adminLevel = adminLevel;
    }
}