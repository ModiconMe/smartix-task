package io.modicon.smartixtask.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.modicon.smartixtask.application.service.SecurityContextHolderService
import io.modicon.smartixtask.application.service.UserAccessService
import io.modicon.smartixtask.application.service.UserManagementService
import io.modicon.smartixtask.domain.model.UserEntity
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails
import io.modicon.smartixtask.infrastructure.security.jwt.JwtAuthFilter
import io.modicon.smartixtask.web.controller.UserController
import io.modicon.smartixtask.web.dto.UserDto
import io.modicon.smartixtask.web.dto.UserRegisterRequest
import io.modicon.smartixtask.web.dto.UserRegisterResponse
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * https://github.com/spockframework/spock/tree/master/spock-spring/boot2-test/src/test/groovy/org/spockframework/boot2
 */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = UserController)
class UserControllerSpockTest extends Specification {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SecurityContextHolderService securityContextHolderService

    @MockBean
    UserAccessService userAccessService

    @SpringBean
    UserManagementService userManagementService = Stub()

//    @Autowired
//    UserManagementService userManagementService

    @MockBean
    JwtAuthFilter jwtAuthFilter

    ObjectMapper objectMapper;

    void setup() {
        objectMapper = new ObjectMapper()
        objectMapper.registerModule(new JavaTimeModule())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    final static String BASE_URL = "/api/v1/users"

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

    def "should register user"() {
        given:
        def request = new UserRegisterRequest(telephone, password)
        def response = new UserRegisterResponse(userDto, token)
        userManagementService.register(request) >> response

        when:
        def actual = mockMvc.perform(post(BASE_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse()

        then:
        verifyAll(actual) {
            status == 200
            getContentAsString(StandardCharsets.UTF_8) == objectMapper.writeValueAsString(response.getUser())
            getHeader(HttpHeaders.AUTHORIZATION) == token
        }
    }

//    @TestConfiguration
//    static class StubConfig {
//        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()
//
//        @Bean
//        UserManagementService userManagementService() {
//            return detachedMockFactory.Stub(UserManagementService)
//        }
//    }
}