package io.modicon.smartixtask.application.service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface PhoneValidationService {
    boolean isValidPhoneNumber(String phone, String countryCode);

    @RequiredArgsConstructor
    @Service
    class Base implements PhoneValidationService {
        @Override
        public boolean isValidPhoneNumber(String phone, String countryCode) {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            return phoneNumberUtil.isPossibleNumber(phone, countryCode);
        }
    }
}
