package io.modicon.smartixtask.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
@Setter
@Entity
@Table(
        indexes = {
                @Index(name = "date_index", columnList = "date")
        }
)
public class Operation {
    @Id
    private String id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "telephone", nullable = false,
            foreignKey = @ForeignKey(name = "user_operation_fk")
    )
    private UserEntity user;
}
