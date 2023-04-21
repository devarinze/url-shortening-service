package com.tinyurl.security.auth;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AuthResponse implements Serializable {
    private static final long serialVersionUID = 1250166508152483573L;
    private BizAuthentication auth;
    String firstName;
    String lastName;
}
