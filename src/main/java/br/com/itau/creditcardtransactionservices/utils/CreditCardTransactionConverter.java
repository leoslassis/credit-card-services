package br.com.itau.creditcardtransactionservices.utils;

import br.com.itau.creditcardtransactionservices.endpoint.resource.CreditCardTransactionResource;
import br.com.itau.creditcardtransactionservices.repository.entity.CreditCardTransactionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CreditCardTransactionConverter {

    private final ModelMapper mapper;

    public CreditCardTransactionConverter(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CreditCardTransactionResource from(CreditCardTransactionEntity entity){
        CreditCardTransactionResource transactionResource = null;
        if (entity != null){
            transactionResource = mapper.map(entity, CreditCardTransactionResource.class);
        }
        return transactionResource;
    }

    public CreditCardTransactionEntity toEntity(CreditCardTransactionResource CreditCardTransactionResource) {
        CreditCardTransactionEntity entity = null;
        if(CreditCardTransactionResource != null){
           entity = mapper.map(CreditCardTransactionResource, CreditCardTransactionEntity.class);
        }
        return entity;
    }

    public Page<CreditCardTransactionResource> from(Page<CreditCardTransactionEntity> transactionEntities) {
        return transactionEntities.map(this::from);
    }
}
