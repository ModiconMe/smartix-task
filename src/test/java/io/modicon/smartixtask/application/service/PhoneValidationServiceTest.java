package io.modicon.smartixtask.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PhoneValidationServiceTest {

    private PhoneValidationService phoneValidationService;

    @BeforeEach
    void setUp() {
        phoneValidationService = new PhoneValidationService.Base();
    }

    @ParameterizedTest
    @CsvSource({
            "+79520009939,true",
            "89520009939,true",
            "+89520009939,false",
            "+8952000993,false",
    })
    void shouldValidate(String phone, boolean result) {
        boolean ru = phoneValidationService.isValidPhoneNumber(phone, "RU");
        assertEquals(ru, result);
    }
}