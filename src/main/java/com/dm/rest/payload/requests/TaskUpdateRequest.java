package com.dm.rest.payload.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class TaskUpdateRequest {
    @NotBlank
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
