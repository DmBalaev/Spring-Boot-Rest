package com.dm.rest.payload.response;

import lombok.Data;

@Data
public class ApplicationResponse {
    private String message;

    public ApplicationResponse(String message) {
        this.message = message;
    }
}
