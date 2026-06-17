package org.prince.upme.security.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwtToken;
    private String userName;

    public LoginResponse(String jwtToken, String userName) {
        this.jwtToken = jwtToken;
        this.userName = userName;
    }
}
