package br.com.itau.creditcardtransactionservices.infra.exception;


import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCode;
import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCodes;

public class ValidationException extends BaseException {

	public ValidationException(final ErrorCode errorCode) {
		super(errorCode);
	}

	public ValidationException(String message, ErrorCodes errorCode) {
		super(message, errorCode);
	}

	public ValidationException(String message) {
		super(message);
	}
}
