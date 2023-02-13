package io.modicon.smartixtask.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Getter
@JsonRootName("user")
public class UserLoginRequest {
    private UserDetails userDetails;
}
