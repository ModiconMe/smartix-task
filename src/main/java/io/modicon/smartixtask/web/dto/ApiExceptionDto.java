package io.modicon.smartixtask.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("error")
public record ApiExceptionDto(String message) {
}
