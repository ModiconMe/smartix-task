package io.modicon.smartixtask.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

public interface PhoneValidationService {
    boolean isValidPhoneNumber(String phone, String countryCode);

    @RequiredArgsConstructor
    @Service
    class Base implements PhoneValidationService {

        private static final String regex = "^((\\+7|8)+(9)+([0-9]){9})$";

        @Override
        public boolean isValidPhoneNumber(String phone, String countryCode) {
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(phone).find();
        }
    }
}
