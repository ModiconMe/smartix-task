package io.modicon.smartixtask.application.service

import io.modicon.smartixtask.application.mapper.UserMapper
import io.modicon.smartixtask.domain.model.UserEntity
import io.modicon.smartixtask.domain.repository.UserRepository
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration
import io.modicon.smartixtask.web.dto.UserDto
import io.modicon.smartixtask.web.dto.UserLoginResponse
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification

class UserAccessServiceSpockTest extends Specification {

    private UserAccessService userAccessService;

    private UserMapper userMapper = new UserMapper();

    def telephone = "telephone"
    def password = "password"
    def currentUser = new CustomUserDetails(telephone, password);
    def user = UserEntity.builder()
            .telephone(telephone)
            .password(password)
            .build()
    def userDto = UserDto.builder()
            .telephone(telephone)
            .build()

    def shouldWork() {
        given: "user login request"
        def details = new CustomUserDetails(telephone, password)
        def token = "token"

        and: "user repository return user"
        def userRepository = Stub(UserRepository) {
            findById(telephone) >> Optional.of(user)
        }

        and: "jwt generation service return token"
        def jwtGeneration = Stub(JwtGeneration) {
            generateAccessToken(currentUser) >> token
        }

        when:
        userAccessService = new UserAccessService.Base(jwtGeneration, userRepository, userMapper)
        def actual = userAccessService.login(details)

        then:
        actual.getUser() == userDto
    }

}
