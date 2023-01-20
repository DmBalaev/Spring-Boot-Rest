package com.dm.rest.util;

import com.dm.rest.payload.response.TaskResponse;
import com.dm.rest.persistance.entity.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskConvectorTest {

    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private TaskConvector convector;

    @Test
    void convertToDto() {
        Task task = mock(Task.class);
        TaskResponse info  = mock(TaskResponse.class);
        when(mapper.map(task, TaskResponse.class)).thenReturn(info);

        TaskResponse expected =  convector.convertToDto(task);

        assertEquals(expected, info);
    }
}