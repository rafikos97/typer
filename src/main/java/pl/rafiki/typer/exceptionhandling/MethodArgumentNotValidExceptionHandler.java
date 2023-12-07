package pl.rafiki.typer.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static pl.rafiki.typer.exceptionhandling.ErrorCode.ARGUMENT_NOT_VALID;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Error methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(request, fieldErrors);
    }

    private Error processFieldErrors(HttpServletRequest request, List<FieldError> fieldErrors) {
        Error error = new Error(BAD_REQUEST.value(), ARGUMENT_NOT_VALID, "Validation error", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), request.getRequestURI());
        for (FieldError fieldError: fieldErrors) {
            error.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }

    @Getter
    static class Error extends ErrorResponse {
        private final List<FieldError> fieldErrors = new ArrayList<>();

        public Error(int statusCode, ErrorCode errorCode, String message, LocalDateTime timestamp, String path) {
            super(statusCode, errorCode, message, timestamp, path);
        }

        public void addFieldError(String objectName, String path, String message) {
            FieldError error = new FieldError(objectName, path, message);
            fieldErrors.add(error);
        }

        public List<String> getFieldErrors() {
            return fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        }
    }
}
