package io.modicon.smartixtask.application.service

import io.modicon.smartixtask.application.mapper.UserMapper
import io.modicon.smartixtask.application.service.UserAccessService
import io.modicon.smartixtask.domain.model.UserEntity
import io.modicon.smartixtask.domain.repository.UserRepository
import io.modicon.smartixtask.infrastructure.exception.ApiException
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration
import io.modicon.smartixtask.web.dto.UserDto
import org.springframework.http.HttpStatus
import spock.lang.Specification

class UserAccessServiceSpockTest extends Specification {

    def userRepository = Stub(UserRepository)
    def jwtGeneration = Stub(JwtGeneration)

    private UserMapper userMapper = new UserMapper()

    private UserAccessService underTest = new UserAccessService.Base(jwtGeneration, userRepository, userMapper)

    def telephone = "telephone"
    def password = "password"
    def currentUser = new CustomUserDetails(telephone, password)
    def user = UserEntity.builder()
            .telephone(telephone)
            .password(password)
            .build()
    def userDto = UserDto.builder()
            .telephone(telephone)
            .build()
    def details = new CustomUserDetails(telephone, password)
    def token = "token"

    def shouldWork() {
        given: "user login request"

        and: "user repository return user"
        userRepository.findById(telephone) >> Optional.of(user)

        and: "jwt generation service return token"
        jwtGeneration.generateAccessToken(currentUser) >> token

        when:
        def actual = underTest.login(details)

        then:
        actual.getUser() == userDto
    }

    def shouldThrownException() {
        given: "user login request"

        and: "user repository not found user"
        userRepository.findById(telephone) >> Optional.empty()

        when:
        underTest.login(details)

        then: "throw exception with 404 status code"
        def e = thrown(ApiException)
        e.getMessage() == "user with telephone number [${telephone}] not found"
        e.getStatus() == HttpStatus.NOT_FOUND
    }


}
