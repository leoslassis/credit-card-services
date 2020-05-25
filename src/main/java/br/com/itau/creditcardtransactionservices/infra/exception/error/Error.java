package br.com.itau.creditcardtransactionservices.infra.exception.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

	private Integer code;
	private String message;
	private String localizedMessage;

	public Error() {
		// Jackson
	}

	public Error(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Error(Integer code, String message, String localizedMessage) {
		this.code = code;
		this.message = message;
		this.localizedMessage = localizedMessage;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLocalizedMessage() {
		return localizedMessage;
	}

	public void setLocalizedMessage(String localizedMessage) {
		this.localizedMessage = localizedMessage;
	}
}
