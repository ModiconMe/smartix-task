package io.modicon.smartixtask.application.service

import io.modicon.smartixtask.application.mapper.UserMapper
import io.modicon.smartixtask.application.service.PhoneValidationService
import io.modicon.smartixtask.application.service.UserManagementService
import io.modicon.smartixtask.domain.model.UserEntity
import io.modicon.smartixtask.domain.repository.UserRepository
import io.modicon.smartixtask.infrastructure.exception.ApiException
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration
import io.modicon.smartixtask.web.dto.UserDto
import io.modicon.smartixtask.web.dto.UserRegisterRequest
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserManagementServiceSpockTest extends Specification {

    def userRepository = Stub(UserRepository)
    def userMapper = Stub(UserMapper)
    def jwtGeneration = Stub(JwtGeneration)
    def phoneValidationService = Stub(PhoneValidationService)

    def passwordEncoder = Stub(PasswordEncoder)

    def underTest = new UserManagementService.Base(
            userRepository,
            userMapper,
            passwordEncoder,
            jwtGeneration,
            phoneValidationService
    )

    def telephone = "telephone"
    def password = "password"
    def user = UserEntity.builder()
            .telephone(telephone)
            .password(password)
            .build()
    def userDto = UserDto.builder()
            .telephone(telephone)
            .build()
    def details = new CustomUserDetails(telephone, password)
    def token = "token"

    def shouldSuccessRegisterUser() {
        given:
        def request = new UserRegisterRequest(telephone, password)
        and: 'Phone number is valid'
        phoneValidationService.isValidPhoneNumber(telephone, "RU") >> true
        and: 'User is not already exist by id'
        userRepository.existsById(telephone) >> false
        and: 'Password encoder encode provided password'
        passwordEncoder.encode(password) >> password
        and: 'Token generated'
        jwtGeneration.generateAccessToken(details) >> token
        and: 'Map user'
        userMapper.apply(user) >> userDto

        when:
        def actual = underTest.register(request)

        then:
        with(actual) {
            token == token
            user == userDto
        }
    }

    def shouldThrown_whenPhoneNumberIsInvalid() {
        given:
        def request = new UserRegisterRequest(telephone, password)
        and: 'Phone number is invalid'
        phoneValidationService.isValidPhoneNumber(telephone, "RU") >> false

        when:
        underTest.register(request)

        then:
        def e = thrown(ApiException)
        with(e) {
            getStatus() == HttpStatus.BAD_REQUEST
            getMessage() == "phone number [$telephone] is invalid"
        }
    }

    def shouldThrown_whenUserAlreadyExist() {
        given:
        def request = new UserRegisterRequest(telephone, password)
        and: 'Phone number is valid'
        phoneValidationService.isValidPhoneNumber(telephone, "RU") >> true
        and: 'User is not already exist by id'
        userRepository.existsById(telephone) >> true

        when:
        underTest.register(request)

        then:
        def e = thrown(ApiException)
        with(e) {
            getStatus() == HttpStatus.BAD_REQUEST
            getMessage() == "user with telephone number [$telephone] already exist"
        }
    }

}
