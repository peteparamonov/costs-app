package pp.dev.costsapp.model.dto;

import pp.dev.costsapp.model.dictionary.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperationSaveDto(
        OperationType operationType,
        String category,
        BigDecimal amount,
        String comment,
        LocalDate operationDate

) {
}
