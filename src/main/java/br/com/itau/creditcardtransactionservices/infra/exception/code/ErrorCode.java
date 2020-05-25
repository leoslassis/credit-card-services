package br.com.itau.creditcardtransactionservices.infra.exception.code;

public interface ErrorCode {
	Integer getCode();

	String getMessage();

	String getLocalizedMessage();
}
