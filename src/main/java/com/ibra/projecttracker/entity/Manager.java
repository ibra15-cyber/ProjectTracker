package com.ibra.projecttracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="managers")
public class Manager {
    @Id
    private Long mangerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
