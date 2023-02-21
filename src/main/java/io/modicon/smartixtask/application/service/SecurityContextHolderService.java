package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface SecurityContextHolderService {

    UserDetails getCurrentUser();

    @Service
    class Base implements SecurityContextHolderService {
        @Override
        public UserDetails getCurrentUser() {
            return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
    }
}
