package io.modicon.smartixtask.application.service

import spock.lang.Specification

class PhoneValidationServiceSpockTest extends Specification {
    void setup() {
    }

    private PhoneValidationService phoneValidationService = new PhoneValidationService.Base();


    def IsValidPhoneNumber() {
        given:
        def phoneNumber1 = "89520009939"
        def phoneNumber2 = "895200099390"

        when:
        def actual1 = phoneValidationService.isValidPhoneNumber(phoneNumber1, "RU")
        def actual2 = phoneValidationService.isValidPhoneNumber(phoneNumber2, "RU")

        then:
        actual1
        !actual2
    }
}
