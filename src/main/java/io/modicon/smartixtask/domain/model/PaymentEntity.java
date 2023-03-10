package io.modicon.smartixtask.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Entity
@Table(
        indexes = {
                @Index(name = "payee_index", columnList = "payee_id")
        }
)
public class PaymentEntity {
    @EqualsAndHashCode.Include
    @Id
    private String id;

    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "payer_id", referencedColumnName = "telephone", nullable = false,
            foreignKey = @ForeignKey(name = "payer_payment_fk")
    )
    private UserEntity payer;

    @ManyToOne
    @JoinColumn(name = "payee_id", referencedColumnName = "telephone", nullable = false,
            foreignKey = @ForeignKey(name = "payee_payment_fk")
    )
    private UserEntity payee;
}
