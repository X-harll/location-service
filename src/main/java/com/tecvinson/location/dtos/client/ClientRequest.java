package com.tecvinson.location.dtos.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRequest {
    @NotBlank(message = "Client name cannot be blank")
    @Size(max = 60, message = "Client name must not exceed 60 characters")
    private String name;

    public @NotBlank(message = "Client name cannot be blank") @Size(max = 60, message = "Client name must not exceed 60 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Client name cannot be blank") @Size(max = 60, message = "Client name must not exceed 60 characters") String name) {
        this.name = name;
    }
}
