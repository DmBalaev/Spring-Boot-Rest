package com.dm.rest.payload.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class TaskRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
