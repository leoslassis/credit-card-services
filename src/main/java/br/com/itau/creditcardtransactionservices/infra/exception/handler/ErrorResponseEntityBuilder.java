package br.com.itau.creditcardtransactionservices.infra.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static br.com.itau.creditcardtransactionservices.infra.exception.error.ErrorResourceBuilder.errorResourceBuilder;

public class ErrorResponseEntityBuilder {

    private HttpStatus httpStatus;
    private Exception exception;
    private Integer code;

    private ErrorResponseEntityBuilder() {
    }

    public static ErrorResponseEntityBuilder errorResponseEntityBuilder() {
        return new ErrorResponseEntityBuilder();
    }

    public ErrorResponseEntityBuilder withException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public ErrorResponseEntityBuilder withCode (Integer code) {
        this.code = code;
        return this;
    }

    public ErrorResponseEntityBuilder withStatus (HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public ResponseEntity build () {
        return ResponseEntity.status(httpStatus).body( errorResourceBuilder()
                .withError(code,
                        exception.getMessage(),
                        exception.getLocalizedMessage()).build());
    }
}

