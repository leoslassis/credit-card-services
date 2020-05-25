package br.com.itau.creditcardtransactionservices.infra.exception.code;

public enum ErrorCodes implements ErrorCode {

	//NOT_FOUND
	ENDPOINT_NOT_FOUND(404001, "Endpoint Not Found"),
	TRANSACTION_NOT_FOUND(404002, "Transaction not found"),

	//BAD_REQUEST
	INVALID_PARAMETER_TYPE(400002, "Invalid parameter type"),
	LOCATION_MATRIX_PARAM_COMBINATION_INVALID(400003, "The combination of matrix params informed is invalid"),


	//UNSUPPORTED_MEDIA_TYPE
	INVALID_MEDIA_TYPE(415000, "Invalid content type");

	private final Integer code;
	private final String message;

	ErrorCodes(final Integer code, final String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getLocalizedMessage() {
		return null;
	}
}
