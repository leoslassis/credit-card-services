package br.com.itau.creditcardtransactionservices.service;

import br.com.itau.creditcardtransactionservices.endpoint.resource.CreditCardTransactionResource;
import br.com.itau.creditcardtransactionservices.infra.exception.ValidationException;
import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCodes;
import br.com.itau.creditcardtransactionservices.repository.CreditCardTransactionRepository;
import br.com.itau.creditcardtransactionservices.repository.entity.CreditCardTransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CreditCardTransactionService {

    @Autowired
    private CreditCardTransactionRepository repository;

    @Autowired
    private GenerateSequenceService generateSequenceService;

    @CacheEvict(value = "campaign")
    public CreditCardTransactionResource createTransaction(CreditCardTransactionResource transactionResource) {
        transactionResource.setId(generateSequenceService.generateSequence(CreditCardTransactionEntity.SEQUENCE_NAME));
        return repository.createTransaction(transactionResource);
    }

    @Cacheable(value = "transaction")
    public CreditCardTransactionResource findById(Long id){
        return repository.findById(id);
    }

    @Cacheable(value = "transaction")
    public Page<CreditCardTransactionResource> findByAll(LocalDate date, Long userId, Long userPaymentMethodId, LocalDate initialDate, LocalDate finalDate, Pageable pageable) {
        if(date != null){
            return repository.findByDate(date, pageable);
        }else if (userId != null && initialDate != null && finalDate != null){
            return repository.findByUserIdAndBetweenDates(userId, initialDate, finalDate, pageable);
        } else if(userId != null){
            return repository.findByUserId(userId, pageable);
        }else if(userPaymentMethodId != null){
            return repository.findByUserPaymentMethodId(userPaymentMethodId, pageable);
        }
        throw new ValidationException(ErrorCodes.LOCATION_MATRIX_PARAM_COMBINATION_INVALID);
    }
}
