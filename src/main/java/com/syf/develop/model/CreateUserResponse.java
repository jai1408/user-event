package com.syf.develop.model;

import lombok.*;

/**
 * Response model for create user request.
 *
 * @see CreateUserRequest
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateUserResponse {
  private String userId;
  private String name;
  private String email;
}
