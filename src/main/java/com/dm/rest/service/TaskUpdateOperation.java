package com.dm.rest.service;

import com.dm.rest.persistance.entity.TaskStatus;
import com.dm.rest.security.CustomUserDetails;

public interface TaskUpdateOperation<T> {
    T updateStatus(Long taskId, TaskStatus status, CustomUserDetails principal);

}
