package com.dm.rest.persistance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;
    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "taskStatus")
    @Column(name = "task_status")
    private TaskStatus taskStatus;
    @Column(name = "creator_name")
    private String creatorName;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "owner_id")
    private User owner;
}
