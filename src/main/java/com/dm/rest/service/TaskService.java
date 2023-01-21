package com.dm.rest.service;

import com.dm.rest.payload.requests.TaskRequest;
import com.dm.rest.payload.requests.TaskUpdateRequest;
import com.dm.rest.payload.response.ApplicationResponse;
import com.dm.rest.persistance.entity.Task;
import com.dm.rest.security.CustomUserDetails;

import java.util.List;

public interface TaskService extends TaskUpdateOperation<ApplicationResponse> {

    Task createTask(TaskRequest request, CustomUserDetails principal);

    Task findById(Long taskId);

    Task updateTask(TaskUpdateRequest request);

    ApplicationResponse deleteTask(Long id);

    List<Task> findAll();

    List<Task> findFreeTask();

    List<Task> findTasksByUser(Long id);

    ApplicationResponse assignTaskToUser(Long taskId, Long userId);

    ApplicationResponse unassignTask(Long taskId);

}
