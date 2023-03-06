package io.modicon.smartixtask.domain.repository

import io.modicon.smartixtask.domain.model.PaymentEntity
import io.modicon.smartixtask.domain.model.UserEntity
import io.modicon.smartixtask.domain.repository.PaymentRepository
import io.modicon.smartixtask.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import java.time.ZonedDateTime

@DataJpaTest
class PaymentRepositorySpockTest extends Specification {

    @Autowired
    UserRepository userRepository

    @Autowired
    PaymentRepository underTest

    UserEntity user1 = UserEntity.builder()
            .telephone("telephone1")
            .password("password1")
            .balance(BigDecimal.valueOf(1000))
            .build()
    UserEntity user2 = UserEntity.builder()
            .telephone("telephone1")
            .password("password1")
            .balance(BigDecimal.valueOf(1000))
            .build()
    PaymentEntity payment1 = PaymentEntity.builder()
            .id(UUID.randomUUID().toString())
            .amount(BigDecimal.valueOf(10))
            .payer(user1)
            .payee(user2)
            .createdAt(ZonedDateTime.now())
            .build()
    PaymentEntity payment2 = PaymentEntity.builder()
            .id(UUID.randomUUID().toString())
            .amount(BigDecimal.valueOf(10))
            .payer(user1)
            .payee(user2)
            .createdAt(ZonedDateTime.now())
            .build()
    PaymentEntity payment3 = PaymentEntity.builder()
            .id(UUID.randomUUID().toString())
            .amount(BigDecimal.valueOf(10))
            .payer(user1)
            .payee(user2)
            .createdAt(ZonedDateTime.now())
            .build()

    def shouldReturnUserPayments() {
        given:
        userRepository.saveAll(List.of(user1, user2))
        underTest.saveAll(List.of(payment1, payment2, payment3))
        int limit = 2

        when:
        def actual = underTest.findByPayee(user2, PageRequest.of(0, limit))

        then:
        actual.size() == limit
    }

}
