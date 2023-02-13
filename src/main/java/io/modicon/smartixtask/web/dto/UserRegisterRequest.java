package io.modicon.smartixtask.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonRootName("user")
public class UserRegisterRequest {
    @NotEmpty
    private String telephone;
    @NotEmpty
    private String password;
}
