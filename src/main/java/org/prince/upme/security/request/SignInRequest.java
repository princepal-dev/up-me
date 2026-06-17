package org.prince.upme.security.request;

import lombok.Data;

@Data
public class SignInRequest {
    private String userName;
    private String password;
}
