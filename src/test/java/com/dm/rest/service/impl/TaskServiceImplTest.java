package com.dm.rest.service.impl;

import com.dm.rest.exceptions.ApiException;
import com.dm.rest.payload.requests.TaskRequest;
import com.dm.rest.payload.requests.TaskUpdateRequest;
import com.dm.rest.persistance.entity.Role;
import com.dm.rest.persistance.entity.Task;
import com.dm.rest.persistance.entity.TaskStatus;
import com.dm.rest.persistance.entity.User;
import com.dm.rest.persistance.repository.TaskRepository;
import com.dm.rest.persistance.repository.UserRepository;
import com.dm.rest.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void createTask() {
        TaskRequest request = TaskRequest.builder()
                .name("name")
                .description("description")
                .build();
        CustomUserDetails principal = mock(CustomUserDetails.class);
        Task task = Task.builder()
                .name("name")
                .description("description")
                .date(LocalDateTime.now())
                .creatorName(principal.getEmail())
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task expected = taskService.createTask(request, principal);

        assertEquals(expected, task);
        assertEquals(expected.getDescription(), request.getDescription());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void findById() {
        Task task = mock(Task.class);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task expected = taskService.findById(task.getId());

        assertEquals(expected, task);
    }

    @Test
    void updateTask() {
        TaskUpdateRequest request = TaskUpdateRequest.builder()
                .id(1L)
                .name("newName")
                .description("newDescription")
                .build();

        Task task = Task.builder()
                .name("oldName")
                .description("oldDesc")
                .build();

        when(taskRepository.findById(request.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task expected = taskService.updateTask(request);

        assertEquals(expected.getName(), "newName");
        assertEquals(expected.getDescription(), "newDescription");
        assertEquals(expected, task);
    }

    @Test
    void deleteTask() {
        Task task = mock(Task.class);

        taskService.deleteTask(task.getId());

        verify(taskRepository).deleteById(task.getId());
    }

    @Test
    void findAll() {
        taskService.findAll();

        verify(taskRepository).findAll();
    }

    @Test
    void findFreeTask() {
        Task t1 = Task.builder()
                .owner(mock(User.class))
                .build();
        Task t2 = Task.builder()
                .taskStatus(TaskStatus.NEW)
                .build();
        List<Task> allTask = List.of(t1, t2);
        when(taskRepository.findAll()).thenReturn(allTask);

       List<Task> expected = taskService.findFreeTask();

       assertEquals(expected.size(), 1);
       verify(taskRepository).findAll();
    }

    @Test
    void findTasksByUser() {
        User user = mock(User.class);
        User user2 = mock(User.class);
        Task t1 = Task.builder()
                .owner(user)
                .build();
        Task t2 = Task.builder()
                .owner(user2)
                .build();
        List<Task> allTask = List.of(t1, t2);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findAll()).thenReturn(allTask);

        List<Task> expected = taskService.findTasksByUser(user.getId());

        assertEquals(expected.size(), 1);
        verify(taskRepository).findAll();
    }

    @Test
    void assignTaskToUser() {
        User user = mock(User.class);
        Task task = new Task();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        taskService.assignTaskToUser(task.getId(), user.getId());

        assertEquals(task.getOwner(), user);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void unassignTask() {
        User user = mock(User.class);
        Task task = Task.builder()
                .owner(user)
                .build();
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        taskService.unassignTask(task.getId());

        assertNull(task.getOwner());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateStatus_NotTaskOwner_AndNotAdmin() {
        User user = mock(User.class);
        Task task = Task.builder()
                .build();
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .authorities(List.of(new Role("ROLE_USER")))
                .build();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(user));


        assertThrows(ApiException.class,
                ()-> taskService.updateStatus(task.getId(), TaskStatus.CLOSE, userDetails));
        assertThrows(ApiException.class,
                ()-> taskService.updateStatus(task.getId(), TaskStatus.REVISION, userDetails));
        assertThrows(ApiException.class,
                ()-> taskService.updateStatus(task.getId(), TaskStatus.NEW, userDetails));
    }

    @Test
    void updateStatus_TaskOwner_AndNotAdmin() {
        User user = mock(User.class);
        Task task = Task.builder()
                .owner(user)
                .taskStatus(TaskStatus.ASSIGNED)
                .build();
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .id(1L)
                .authorities(List.of(new Role("ROLE_USER")))
                .build();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userRepository.findById(userDetails.getId())).thenReturn(Optional.of(user));
        when(taskRepository.findAll()).thenReturn(List.of(task));

        taskService.updateStatus(task.getId(), TaskStatus.COMPLETED, userDetails);
        assertEquals(task.getTaskStatus(), TaskStatus.COMPLETED);

    }

    @Test
    void updateStatus_NotTaskOwner_Admin() {
        Task task = Task.builder()
                .taskStatus(TaskStatus.NEW)
                .build();
        CustomUserDetails userDetails = CustomUserDetails.builder()
                .authorities(List.of(new Role("ROLE_ADMIN")))
                .build();

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        taskService.updateStatus(task.getId(), TaskStatus.CLOSE, userDetails);
        assertEquals(task.getTaskStatus(), TaskStatus.CLOSE);

        taskService.updateStatus(task.getId(), TaskStatus.REVISION, userDetails);
        assertEquals(task.getTaskStatus(), TaskStatus.REVISION);
    }
}