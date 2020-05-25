package br.com.itau.creditcardtransactionservices.infra.exception.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Collections;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResource {

	private Collection<Error> errors;

	public ErrorResource() {
		// Jackson
	}

	public ErrorResource(final Collection<Error> errors) {
		this.errors = errors;
	}

	public static ErrorResource build(Integer code, String message, String localizedMessage) {
		return new ErrorResource(Collections.singletonList(new Error(code, message, localizedMessage)));
	}

	public Collection<Error> getErrors() {
		return errors;
	}

	public void setErrors(final Collection<Error> errors) {
		this.errors = errors;
	}
}