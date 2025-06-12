package com.ibra.projecttracker.entity;

import com.ibra.projecttracker.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

//    @NotBlank(message = "first name can't be blank")
//    @Size(max = 15)
    private String firstName;

//    @NotBlank(message = "first name can't be blank")
//    @Size( max = 15)
    private String lastName;

//    @NotBlank(message = "Phone number must be entered")
//    @Size(max = 10)
    private String phoneNumber;

//    @Column(unique = true)
//    @Email()
//    @NotBlank(message = "Email can not be blank")
    private String email;

//    @NotBlank(message = "password can't be blank")
//    @Size(min = 6, max = 100)
    private String password;


    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(optional = true, mappedBy = "user")
    private Admin admin;

    @OneToOne(optional = true, mappedBy = "user")
    private Developer developer;

    @OneToOne(optional = true, mappedBy = "user")
    private Contractor contractor;

    @OneToOne(optional = true, mappedBy = "user")
    private Manager manager;
}
