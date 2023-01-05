package com.dm.rest.payload.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class TaskUpdateRequest {
    private Long id;
    private String name;
    private String description;
}
