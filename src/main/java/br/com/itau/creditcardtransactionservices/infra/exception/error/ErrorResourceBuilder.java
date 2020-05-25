package br.com.itau.creditcardtransactionservices.infra.exception.error;

import java.util.Collections;

public class ErrorResourceBuilder {

    private ErrorResource errorResource = new ErrorResource();

    private ErrorResourceBuilder() {
    }

    public static ErrorResourceBuilder errorResourceBuilder() {
        return new ErrorResourceBuilder();
    }

    public ErrorResourceBuilder withError (Integer code, String message, String localizedMessage) {
        errorResource.setErrors(Collections.singletonList(new Error(code, message, localizedMessage)));
        return this;
    }

    public ErrorResource build () {
        return this.errorResource;
    }
}
