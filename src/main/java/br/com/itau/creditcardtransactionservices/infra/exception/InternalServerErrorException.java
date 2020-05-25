package br.com.itau.creditcardtransactionservices.infra.exception;

public class InternalServerErrorException extends BaseException {
	public InternalServerErrorException(String message, Exception e) {
		super(message, e);
	}
}
