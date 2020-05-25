package br.com.itau.creditcardtransactionservices.infra.exception;

import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCode;
import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCodes;

public abstract class BaseException extends RuntimeException {
	private Integer code;
	private String message;
	private String localizedMessage;

	protected BaseException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.setCode(errorCode.getCode());
		this.setMessage(errorCode.getMessage());
		this.setLocalizedMessage(errorCode.getLocalizedMessage());
	}
	protected BaseException(final String message, final Exception e) {
		super(message, e);
		this.setMessage(message);
	}

	protected BaseException(final String message) {
		super(message);
		setMessage(message);
	}

	public BaseException(String message, ErrorCodes errorCode) {
		super(message);
		setMessage(message);
		this.setCode(errorCode.getCode());
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getLocalizedMessage() {
		return localizedMessage;
	}

	public void setLocalizedMessage(String localizedMessage) {
		this.localizedMessage = localizedMessage;
	}
}