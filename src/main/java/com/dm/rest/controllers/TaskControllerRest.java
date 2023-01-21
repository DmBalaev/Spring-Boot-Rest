package com.dm.rest.controllers;

import com.dm.rest.payload.requests.TaskRequest;
import com.dm.rest.payload.requests.TaskUpdateRequest;
import com.dm.rest.payload.response.ApplicationResponse;
import com.dm.rest.payload.response.TaskResponse;
import com.dm.rest.persistance.entity.TaskStatus;
import com.dm.rest.security.CustomUserDetails;
import com.dm.rest.service.TaskService;
import com.dm.rest.util.TaskConvector;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            tags = {"Task operations"},
            summary = "Return all task (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get all tasks"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskResponse> allTasks(){
        return convector.convertAllToDto(taskService.findAll());
    }


    @Operation(
            tags = {"Task operations"},
            summary = "Return all free tasks",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get free tasks")
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @GetMapping("/freeTasks")
    public List<TaskResponse> freeTasks(){
        return convector.convertAllToDto(taskService.findFreeTask());
    }


    @Operation(
            tags = {"Task operations"},
            summary = "Return all task by user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get tasks by User"),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @GetMapping("/{userId}")
    public List<TaskResponse> tasksOwnedUser(@PathVariable Long userId){
        return convector.convertAllToDto(taskService.findTasksByUser(userId));
    }

    @Operation(
            tags = {"Task operations"},
            summary = "Create task(Only ADMIN privileges)",
            description = "This is endpoint for create task, can use USER with ADMIN privileges",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful created task"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request,
                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(convector.convertToDto(taskService.createTask(request, currentUser)));
    }

    @Operation(
            tags = {"Task operations"},
            summary = "Update info task (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful created task"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PutMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(convector.convertToDto(taskService.updateTask(request)));
    }


    @Operation(
            tags = {"Task operations"},
            summary = "Assign a task to user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful user assignment"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Task or user not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PutMapping("/{taskId}/assign/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<ApplicationResponse> assignUser(@PathVariable Long taskId,
                                                          @PathVariable Long userId){
        ApplicationResponse response = taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(response);
    }


    @Operation(
            tags = {"Task operations"},
            summary = "Cancel the task from the user (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful detachment from the task from the user"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PutMapping("/{taskId}/unassign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationResponse> unassign(@PathVariable Long taskId){
        ApplicationResponse response = taskService.unassignTask(taskId);
        return ResponseEntity.ok(response);
    }


    @Operation(
            tags = {"Task operations"},
            summary = "Change task status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful change status"),
                    @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @PutMapping("/{taskId}/change_status")
    public ResponseEntity<ApplicationResponse> changeStatus(@PathVariable Long taskId,
                                                            @AuthenticationPrincipal CustomUserDetails principal,
                                                            @RequestBody JsonNode json){
        TaskStatus status = TaskStatus.valueOf(json.get("taskStatus").asText());
        ApplicationResponse response = taskService.updateStatus(taskId, status, principal);
        return ResponseEntity.ok(response);
    }


    @Operation(
            tags = {"Task operations"},
            summary = "Delete task (Only ADMIN privileges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful delete task"),
                    @ApiResponse(responseCode = "403", description = "Has no authority", content = @Content),
            },
            security = @SecurityRequirement(name = "BearerJWT")
    )
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationResponse> deleteTask(@PathVariable Long id) {
        ApplicationResponse response = taskService.deleteTask(id);
        return ResponseEntity.ok(response);
    }
}
