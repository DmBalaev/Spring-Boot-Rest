package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.exceptions.UserNotFoundException;
import com.dm.rest.payload.requests.TaskRequest;
import com.dm.rest.payload.requests.TaskUpdateRequest;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.persistance.entity.Task;
import com.dm.rest.persistance.entity.TaskStatus;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.TaskRepository;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Task createTask(TaskRequest request, CustomUserDetails principal) {
        Task task = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .date(LocalDateTime.now())
                .creatorName(principal.getEmail())
                .taskStatus(TaskStatus.NEW)
                .build();
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()-> new ApiException("Task with id '" + id + "'", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public Task updateTask(TaskUpdateRequest request) {
        Task task = findById(request.getId());
        task.setName(request.getName());
        task.setDescription(request.getDescription());

        return taskRepository.save(task);
    }

    @Override
    public ApiResponse deleteTask(Long id) {
        taskRepository.deleteById(id);
        return new ApiResponse("You successfully delete task");
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findFreeTask() {
        return taskRepository.findAll().stream()
                .filter(task -> task.getOwner() == null && task.getTaskStatus().equals(TaskStatus.NEW))
                .toList();
    }

    @Override
    public List<Task> findTasksByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User witn id '" + userId + "' not found."));
        return findAll().stream()
                .filter(task -> Objects.nonNull(task.getOwner()) && task.getOwner().equals(user))
                .toList();
    }

    @Override
    public ApiResponse assignTaskToUser(Long taskId, Long userId) {
        Task task = findById(taskId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User witn id '" + userId + "' not found."));

        task.setOwner(user);
        task.setTaskStatus(TaskStatus.ASSIGNED);
        taskRepository.save(task);

        return new ApiResponse("You successfully assign task");
    }

    @Override
    public ApiResponse unassignTask(Long taskId) {
        Task task = findById(taskId);
        task.setOwner(null);
        taskRepository.save(task);
        return new ApiResponse("You successfully unassign task");
    }

    @Override
    public ApiResponse updateStatus(Long taskId, TaskStatus status, CustomUserDetails principal) {
        Task task = findById(taskId);

        if (isNotAdmin(principal)&&
                isOwner(principal, task) &&
                isNotAllowedStatusForUser(status)){
             throw new ApiException("Tis is not your task or not permission", HttpStatus.FORBIDDEN);
         }
        task.setTaskStatus(status);
        taskRepository.save(task);
        return new ApiResponse("You successfully change task status");
    }

    private boolean isOwner(CustomUserDetails principal, Task task) {
        return findTasksByUser(principal.getId()).contains(task);
    }

    private  boolean isNotAdmin(CustomUserDetails principal) {
        return principal.getAuthorities().stream()
                .noneMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }

    private  boolean isNotAllowedStatusForUser(TaskStatus status) {
        return status.name().equals("NEW") ||
                status.name().equals("ASSIGNED") ||
                status.name().equals("REVISION") ||
                status.name().equals("CLOSE");
    }

}