package br.com.itau.creditcardtransactionservices.service;

import br.com.itau.creditcardtransactionservices.endpoint.resource.CreditCardTransactionResource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardTransactionConsumer {

    @Autowired
    private CreditCardTransactionService service;

    @RabbitListener(queues = "${queue.credit-card.transaction}")
    public void receiveMessage(CreditCardTransactionResource transaction){
        service.createTransaction(transaction);
    }

}
