package com.dm.rest.payload.response;

import com.dm.rest.persistance.entity.User;
import java.time.LocalDateTime;

public class TaskResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;
    private boolean isCompleted;
    private String creatorName;
    private User owner;
}
