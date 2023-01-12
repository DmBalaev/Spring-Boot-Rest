package com.dm.rest.controllers;

import com.dm.rest.payload.requests.TaskRequest;
import com.dm.rest.payload.requests.TaskUpdateRequest;
import com.dm.rest.payload.response.ApiResponse;
import com.dm.rest.payload.response.TaskResponse;
import com.dm.rest.persistance.entity.Task;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.TaskService;
import com.dm.rest.util.TaskConvector;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskControllerRest {
    private final TaskService taskService;
    private final TaskConvector convector;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskResponse> allTasks(){
        return convector.convertAllToDto(taskService.findAll());
    }

    @GetMapping("/freeTasks")
    public List<TaskResponse> freeTasks(){
        return convector.convertAllToDto(taskService.findFreeTask());
    }

    @GetMapping("/{userId}/")
    public List<TaskResponse> tasksOwnedUser(@PathVariable Long userId){
        return convector.convertAllToDto(taskService.findTasksByUser(userId));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@Valid TaskRequest request,
                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(convector.convertToDto(taskService.createTask(request, currentUser)));
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> updateTask(@Valid TaskUpdateRequest request) {
        return ResponseEntity.ok(convector.convertToDto(taskService.updateTask(request)));
    }

    @PutMapping("/{taskId}/assign/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.principal.username")
    public ResponseEntity<ApiResponse> assignUser(@PathVariable Long taskId,
                                                  @PathVariable Long userId){
        ApiResponse response = taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}/unassign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> unassign(@PathVariable Long taskId){
        ApiResponse response = taskService.unassignTask(taskId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}/done")
    public ResponseEntity<ApiResponse> setDone(@PathVariable Long taskId){
        ApiResponse response = taskService.setTaskCompleted(taskId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}/not_done")
    public ResponseEntity<ApiResponse> unsetDone(@PathVariable Long taskId){
        ApiResponse response = taskService.setTaskNotCompleted(taskId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteTask(@PathVariable Long id) {
        ApiResponse response = taskService.deleteTask(id);
        return ResponseEntity.ok(response);
    }
}
