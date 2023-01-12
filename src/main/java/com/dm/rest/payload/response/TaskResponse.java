package com.dm.rest.payload.response;

import com.dm.rest.persistance.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;
    private boolean isCompleted;
    private String creatorName;
    private User owner;
}
