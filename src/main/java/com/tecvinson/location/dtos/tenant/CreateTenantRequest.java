package com.tecvinson.location.dtos.tenant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTenantRequest {
    @NotBlank(message = "Tenant name cannot be blank")
    @Size(max = 60, message = "Tenant name must not exceed 60 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;


    public @NotBlank(message = "Tenant name cannot be blank") @Size(max = 60, message = "Tenant name must not exceed 60 characters") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Tenant name cannot be blank") @Size(max = 60, message = "Tenant name must not exceed 60 characters") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }
}
