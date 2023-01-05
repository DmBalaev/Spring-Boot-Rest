package com.dm.rest.payload.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class TaskRequest {
    private String name;
    private String description;
}
