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
                @Index(name = "date_index", columnList = "date"),
                @Index(name = "userTelephone_index", columnList = "userTelephone")
        }
)
public class Operation {
    @Id
    private String id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String userTelephone;

    @Column(nullable = false)
    private BigDecimal sum;
}
