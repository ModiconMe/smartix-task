package io.modicon.smartixtask.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"payments"})
@Getter
@Entity
public class UserEntity {

    @EqualsAndHashCode.Include
    @Id
    private String telephone;

    @Column(nullable = false)
    private String password;
    private BigDecimal balance;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private LocalDate dateOfBirth;

    @Singular
    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    private List<PaymentEntity> payments;
}
