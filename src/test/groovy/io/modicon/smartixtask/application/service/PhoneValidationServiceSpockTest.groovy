package io.modicon.smartixtask.application.service

import io.modicon.smartixtask.application.service.PhoneValidationService
import spock.lang.Specification

class PhoneValidationServiceSpockTest extends Specification {

    private PhoneValidationService phoneValidationService = new PhoneValidationService.Base();


    def "should return valid(true) #telephones telephone number"() {
        when:
        def actual = phoneValidationService.isValidPhoneNumber(telephones.each{it}, "RU")

        then:
        actual

        where:
        telephones << ["89520009939", "+79520009939"]
    }

    def "should return invalid(false) #telephones telephone number"() {
        when:
        def actual = phoneValidationService.isValidPhoneNumber(telephones.each{it}, "RU")

        then:
        !actual

        where:
        telephones << ["+89520009939", "79520009939",]
    }

}
