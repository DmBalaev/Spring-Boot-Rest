package com.dm.rest.util;

import com.dm.rest.payload.response.TaskResponse;
import com.dm.rest.persistance.entity.Task;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


@Component
@RequiredArgsConstructor
public class TaskConvector {
    private final ModelMapper mapper;

    public TaskResponse convertToDto(Task task){
        return mapper.map(Task.class, TaskResponse.class);
    }

    public List<TaskResponse> convertAllToDto(Collection<Task> tasks){
        return tasks.stream()
                .map(this::convertToDto)
                .toList();
    }
}
