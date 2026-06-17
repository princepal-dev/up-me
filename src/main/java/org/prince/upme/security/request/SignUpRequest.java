package org.prince.upme.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank
    @Size (max = 50)
    private String userName;

    @Email
    @NotBlank
    @Size (max = 50)
    private String email;

    @NotBlank
    @Size (max = 50)
    private String password;
}
