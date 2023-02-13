package io.modicon.smartixtask.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Getter
public class UserLoginRequest {
    private UserDetails userDetails;
}
