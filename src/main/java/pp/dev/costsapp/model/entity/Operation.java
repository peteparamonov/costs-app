package pp.dev.costsapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import pp.dev.costsapp.model.dictionary.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "operation")
@NoArgsConstructor
@Data
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_type")
    @Enumerated(value = EnumType.STRING)
    private OperationType operationType;

    @Column(name = "category")
    private String category;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "operation_date")
    private LocalDate operationDate;

    @Column(name = "wallet_id")
    private Long walletId;
}
