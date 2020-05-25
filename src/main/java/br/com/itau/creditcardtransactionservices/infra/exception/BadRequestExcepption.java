package br.com.itau.creditcardtransactionservices.infra.exception;

import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCode;
import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCodes;

public class BadRequestExcepption extends BaseException {

    public BadRequestExcepption(final ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestExcepption(final String message) {
        super(message);
    }

    public BadRequestExcepption(String message, ErrorCodes errorCodes) {
        super(message, errorCodes);
    }
}