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
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String password = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

            return new CustomUserDetails(username, password);
        }
    }
}
