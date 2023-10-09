package com.auth0.example.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String name;
    private String email;
}
