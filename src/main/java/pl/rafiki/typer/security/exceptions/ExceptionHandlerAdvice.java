package pl.rafiki.typer.security.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.rafiki.typer.utils.ErrorResponse;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidCredentialsException invalidCredentialsException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(invalidCredentialsException.getSTATUS_CODE().value())
                .errorCode(invalidCredentialsException.getERROR_CODE())
                .message(invalidCredentialsException.getMessage())
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getStatusCode()));
    }
}

