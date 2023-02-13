package io.modicon.smartixtask.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonRootName("user")
public class UserRegisterResponse {
    private String telephone;
    private String token;
}
