package br.com.itau.creditcardtransactionservices.repository.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Document(collection = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardTransactionEntity {

    @Transient
    public static final String SEQUENCE_NAME = "transactions_sequence";

    @Id
    private Long id;

    @NotBlank
    private Long userPaymentMethodId;

    @NotBlank
    private Long userId;

    @NotBlank
    private LocalDate date;

}