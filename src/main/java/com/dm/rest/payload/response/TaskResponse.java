package com.dm.rest.payload.response;

import com.dm.rest.persistance.entity.TaskStatus;
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
    private TaskStatus taskStatus;
    private String creatorName;
    private UserInfo owner;
}
