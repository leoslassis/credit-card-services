package br.com.itau.creditcardtransactionservices.repository;

import br.com.itau.creditcardtransactionservices.repository.entity.CreditCardTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CreditCardTransactionMongoRepository extends MongoRepository<CreditCardTransactionEntity, String> {

    Page<CreditCardTransactionEntity> findByDate(LocalDate date, Pageable pageable);

    Page<CreditCardTransactionEntity> findByUserId(Long userId, Pageable pageable);

    Page<CreditCardTransactionEntity> findByUserPaymentMethodId(Long userPaymentMethodId, Pageable pageable);

    Page<CreditCardTransactionEntity> findByUserIdAndDateBetween(Long userId, LocalDate initialDate, LocalDate finalDate, Pageable pageable);

    Optional<CreditCardTransactionEntity> findById(Long id);
}

