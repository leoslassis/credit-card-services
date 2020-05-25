package br.com.itau.creditcardtransactionservices.repository;

import br.com.itau.creditcardtransactionservices.endpoint.resource.CreditCardTransactionResource;
import br.com.itau.creditcardtransactionservices.infra.exception.NotFoundException;
import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCodes;
import br.com.itau.creditcardtransactionservices.repository.entity.CreditCardTransactionEntity;
import br.com.itau.creditcardtransactionservices.utils.CreditCardTransactionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class CreditCardTransactionRepository {

    @Autowired
    private CreditCardTransactionMongoRepository mongoRepository;

    @Autowired
    private CreditCardTransactionConverter converter;

    public CreditCardTransactionResource createTransaction(CreditCardTransactionResource transactionResource) {
        return converter.from(mongoRepository.save(converter.toEntity(transactionResource)));
    }

    public Page<CreditCardTransactionResource> findByDate(LocalDate date, Pageable pageable) {
        return converter.from(mongoRepository.findByDate(date, pageable));
    }

    public Page<CreditCardTransactionResource> findByUserId(Long userId, Pageable pageable) {
        return converter.from(mongoRepository.findByUserId(userId, pageable));
    }

    public Page<CreditCardTransactionResource> findByUserPaymentMethodId(Long userPaymentMethodId, Pageable pageable) {
        return converter.from(mongoRepository.findByUserPaymentMethodId(userPaymentMethodId, pageable));
    }

    public CreditCardTransactionResource findById(Long id) {
        CreditCardTransactionEntity creditCardTransactionEntity =
                mongoRepository.findById(id).orElseThrow(() ->
                        new NotFoundException(ErrorCodes.TRANSACTION_NOT_FOUND));
        return converter.from(creditCardTransactionEntity);
    }

    public Page<CreditCardTransactionResource> findByUserIdAndBetweenDates(Long userId, LocalDate initialDate, LocalDate finalDate, Pageable pageable) {
        return converter.from(mongoRepository.findByUserIdAndDateBetween(userId, initialDate, finalDate, pageable));
    }
}
