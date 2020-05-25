package br.com.itau.creditcardtransactionservices;

import br.com.itau.creditcardtransactionservices.repository.CreditCardTransactionMongoRepository;
import br.com.itau.creditcardtransactionservices.repository.entity.CreditCardTransactionEntity;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LocalhostMongoConfiguration.class)
public class CreditCardTransactionMongoIntegrationTest {
	
	 @Autowired
	 CreditCardTransactionMongoRepository repository;
	  
	 @Autowired
     MongoTemplate template;
	 
	 @Before public void setUp() {
		 
		 template.dropCollection("creditCardTransaction");
		 template.createCollection("creditCardTransaction");
		 
		 repository.save(new CreditCardTransactionEntity(1L, 123L, 321L, LocalDate.now()));
		 repository.save(new CreditCardTransactionEntity(2L, 234L, 321L, LocalDate.now()));
		 repository.save(new CreditCardTransactionEntity(3L, 456L, 987L, LocalDate.now()));
		 repository.save(new CreditCardTransactionEntity(4L, 789L, 798L, LocalDate.now()));

	 }

	@Test
	public void givenTransactionWhenSaveThenCreateNewTransaction() {

		Assertions.assertThat(repository.findAll().size()).isGreaterThan(0);
	}

	@Test
	public void givenTransactionWhenSaveThenTransactionGetByDate() {

		Page<CreditCardTransactionEntity> transactionEntities = repository.findByDate(LocalDate.now(), Pageable.unpaged());

		Assertions.assertThat(transactionEntities.getSize()).isGreaterThan(0);
		Assertions.assertThat(transactionEntities.getContent().get(0).getUserId()).isEqualTo(321L);
		Assertions.assertThat(transactionEntities.getContent().get(0).getUserPaymentMethodId()).isEqualTo(123L);
	}

	@Test
	public void givenTransactionWhenSaveThenTransactionGetByUserId() {

		Page<CreditCardTransactionEntity> transactionEntities = repository.findByUserId(987L, Pageable.unpaged());

		Assertions.assertThat(transactionEntities.getSize()).isGreaterThan(0);
		Assertions.assertThat(transactionEntities.getContent().get(0).getId()).isEqualTo(3L);
		Assertions.assertThat(transactionEntities.getContent().get(0).getUserPaymentMethodId()).isEqualTo(456L);
	}

	@Test
	public void givenTransactionWhenSaveThenTransactionGetByUserIdNotFound() {

		Page<CreditCardTransactionEntity> transactionEntities = repository.findByUserId(2L, Pageable.unpaged());

		Assertions.assertThat(transactionEntities.getSize()).isZero();
	}

	@Test
	public void givenTransactionWhenSaveThenTransactionGetByUserPaymentMethodIdNotFound() {

		Page<CreditCardTransactionEntity> transactionEntities = repository.findByUserPaymentMethodId(3L, Pageable.unpaged());

		Assertions.assertThat(transactionEntities.getSize()).isZero();
	}

	@Test
	public void givenTransactionWhenSaveThenTransactionGetByUserPaymentMethodId() {

		Page<CreditCardTransactionEntity> transactionEntities = repository.findByUserPaymentMethodId(234L, Pageable.unpaged());

		Assertions.assertThat(transactionEntities.getSize()).isGreaterThan(0);
		Assertions.assertThat(transactionEntities.getContent().get(0).getId()).isEqualTo(2L);
		Assertions.assertThat(transactionEntities.getContent().get(0).getUserId()).isEqualTo(321L);
	}

	@Test
	public void givenTransactionWhenSaveThenTransactionGetByTransactionId() {
		Optional<CreditCardTransactionEntity> transactionEntity = repository.findById(4L);

		Assertions.assertThat(transactionEntity).isNotEmpty();
		Assertions.assertThat(transactionEntity.get().getUserId()).isEqualTo(798L);
		Assertions.assertThat(transactionEntity.get().getUserPaymentMethodId()).isEqualTo(789L);
	}

	@Test
	public void givenTransactionWhenSaveThenTransactionGetByTransactionIdNotFound() {
		Optional<CreditCardTransactionEntity> transactionEntity = repository.findById(5L);

		Assertions.assertThat(transactionEntity).isEmpty();
	}
	 
	 @After public void tearDown() {
		 repository.deleteAll();
	 }
	 
}
