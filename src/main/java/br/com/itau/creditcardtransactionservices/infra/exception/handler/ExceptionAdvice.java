package br.com.itau.creditcardtransactionservices.infra.exception.handler;

import br.com.itau.creditcardtransactionservices.infra.exception.InternalServerErrorException;
import br.com.itau.creditcardtransactionservices.infra.exception.NotFoundException;
import br.com.itau.creditcardtransactionservices.infra.exception.ValidationException;
import br.com.itau.creditcardtransactionservices.infra.exception.code.ErrorCodes;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static br.com.itau.creditcardtransactionservices.infra.exception.handler.ErrorResponseEntityBuilder.errorResponseEntityBuilder;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionAdvice extends ExceptionHandlerExceptionResolver {

	private HttpServletRequest req;

	public ExceptionAdvice(HttpServletRequest req) {
		this.req = req;
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity handleException(final NotFoundException exception) {
		req.setAttribute("throwable", exception);
		return errorResponseEntityBuilder()
				.withException(exception)
				.withCode(exception.getCode())
				.withStatus(HttpStatus.NOT_FOUND)
				.build();
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity handleException(ValidationException exception) {
		req.setAttribute("throwable", exception);
		return errorResponseEntityBuilder()
				.withException(exception)
				.withCode(exception.getCode())
				.withStatus(HttpStatus.BAD_REQUEST)
				.build();
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity handleException(MethodArgumentTypeMismatchException exception) {
		req.setAttribute("throwable", exception);
		String message = String.format("Property '%s' must be '%s' type", exception.getName(), Objects.requireNonNull(exception.getRequiredType()).getSimpleName());

		return errorResponseEntityBuilder()
				.withException(new ValidationException(message))
				.withCode(ErrorCodes.INVALID_PARAMETER_TYPE.getCode())
				.withStatus(HttpStatus.BAD_REQUEST)
				.build();
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity handleException(final NoHandlerFoundException exception) {
		req.setAttribute("throwable", exception);
		return errorResponseEntityBuilder()
				.withException(new NotFoundException(exception.getMessage(), ErrorCodes.ENDPOINT_NOT_FOUND))
				.withStatus(HttpStatus.NOT_FOUND)
				.build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity handleException(final Exception exception) {
		req.setAttribute("throwable", exception);
		return errorResponseEntityBuilder()
				.withException(new InternalServerErrorException(exception.getMessage(), exception))
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.build();
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity handleInvalidFormatException(final InvalidFormatException exception) {
		req.setAttribute("throwable", exception);
		String message = String.format("Value '%s' of property '%s' is not a valid %s value",
				exception.getValue(),
				exception.getPath().get(0).getFieldName(),
				exception.getTargetType().getSimpleName());

		return errorResponseEntityBuilder()
				.withException(new ValidationException(message))
				.withStatus(HttpStatus.BAD_REQUEST)
				.build();
	}

	@ExceptionHandler(UnrecognizedPropertyException.class)
	public ResponseEntity handleUnrecognizedPropertyException(final UnrecognizedPropertyException exception) {
		req.setAttribute("throwable", exception);
		String message = String.format("Property '%s' doesn't exist. The expected properties are: %s",
				exception.getPropertyName(), exception.getKnownPropertyIds());

		return errorResponseEntityBuilder()
				.withException(new ValidationException(message))
				.withStatus(HttpStatus.BAD_REQUEST)
				.build();
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException exception) {
		req.setAttribute("throwable", exception);
		return errorResponseEntityBuilder()
				.withException(exception)
				.withCode(ErrorCodes.INVALID_MEDIA_TYPE.getCode())
				.withStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
				.build();
	}

	@ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
	public ResponseEntity handleHttpMediaTypeNotAcceptableException(final HttpMediaTypeNotAcceptableException exception) {
		req.setAttribute("throwable", exception);
		return errorResponseEntityBuilder().withException(exception).withStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
	}

	@ExceptionHandler(ServletRequestBindingException.class)
	public ResponseEntity handleServletRequestBindingException(final ServletRequestBindingException exception) {
		req.setAttribute("throwable", exception);
		return errorResponseEntityBuilder().withException(exception).withStatus(HttpStatus.BAD_REQUEST).build();
	}

}
