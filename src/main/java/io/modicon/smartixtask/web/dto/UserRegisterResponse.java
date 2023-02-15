package io.modicon.smartixtask.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserRegisterResponse {
    private UserDto user;
    private String token;
}
