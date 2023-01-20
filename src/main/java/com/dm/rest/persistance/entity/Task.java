package com.dm.rest.persistance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;
    private String creatorName;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
