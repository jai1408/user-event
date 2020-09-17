package com.syf.develop.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String userId;
    private String name;
    private String email;
    private String password;
    private String encryptedPassword;
    private Instant createdDate;
    private boolean enabled;
}
