package br.com.itau.creditcardtransactionservices.endpoint;

import br.com.itau.creditcardtransactionservices.endpoint.resource.CreditCardTransactionResource;
import br.com.itau.creditcardtransactionservices.service.CreditCardTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Api(tags = "Transaction API")
@RestController
@RequestMapping(value = "/rs/credit-card",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditCardTransactionEndpoint {

    @Autowired
    private CreditCardTransactionService service;

    @ApiOperation(value = "Cadastrar Transação")
    @PostMapping(value = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditCardTransactionResource> createTransaction(@RequestBody CreditCardTransactionResource transactionResource){
        return ResponseEntity.ok(service.createTransaction(transactionResource));
    }

    @ApiOperation(value = "Retornar todas Transações de acordo com o filtro")
    @GetMapping("/transaction{matrix}")
    public ResponseEntity<Page<CreditCardTransactionResource>> findAll(
            @MatrixVariable(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @MatrixVariable(value = "userId", required = false) Long userId,
            @MatrixVariable(value = "userPaymentMethodId", required = false) Long userPaymentMethodId,
            @MatrixVariable(value = "initialDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate initialDate,
            @MatrixVariable(value = "finalDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finalDate,
            Pageable pageable){
        return ResponseEntity.ok(service.findByAll(date, userId, userPaymentMethodId, initialDate, finalDate, pageable));
    }


    @ApiOperation(value = "Retornar Transação por ID")
    @GetMapping("/transaction/{id}")
    public ResponseEntity<CreditCardTransactionResource> findById(
            @PathVariable(value = "id", required = true) Long id,
            Pageable pageable){
        return ResponseEntity.ok(service.findById(id));
    }

}
