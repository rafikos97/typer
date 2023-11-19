package pl.rafiki.typer.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.security.exceptions.InvalidCredentialsException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
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

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    ResponseEntity<ErrorResponse> handleInvalidBearerTokenException() {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(UNAUTHORIZED.value())
                .errorCode("INVALID_TOKEN")
                .message("Provided access token is expired, revoked, malformed or invalid!")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseEntity<ErrorResponse> handleAccessDeniedException() {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(FORBIDDEN.value())
                .errorCode("NO_PERMISSION")
                .message("User has no permission to access this resource!")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ErrorResponse> handleOtherException() {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .errorCode("OTHER_ERROR")
                .message("A server internal error occurs!")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }
}

