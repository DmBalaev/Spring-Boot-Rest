package com.dm.rest.service;

import com.dm.rest.payload.requests.TaskRequest;
import com.dm.rest.payload.requests.TaskUpdateRequest;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.persistance.entity.Task;
import com.dm.rest.security.CustomUserDetails;

import java.util.List;

public interface TaskService {

    Task createTask(TaskRequest request, CustomUserDetails principal);

    Task findById(Long taskId);

    Task updateTask(TaskUpdateRequest request);

    ApiResponse deleteTask(Long id);

    List<Task> findAll();

    List<Task> findFreeTask();

    List<Task> findTasksByUser(Long id);

    ApiResponse setTaskCompleted(Long id);

    ApiResponse setTaskNotCompleted(Long id);

    ApiResponse assignTaskToUser(Long taskId, Long userId);

    ApiResponse unassignTask(Long taskId);

}
