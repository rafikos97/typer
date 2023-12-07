package pl.rafiki.typer.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.rafiki.typer.security.exceptions.InvalidCredentialsException;
import pl.rafiki.typer.security.exceptions.RefreshTokenException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpStatus.*;
import static pl.rafiki.typer.exceptionhandling.ErrorCode.*;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, InvalidCredentialsException invalidCredentialsException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(UNAUTHORIZED.value())
                .errorCode(invalidCredentialsException.getErrorCode())
                .message(invalidCredentialsException.getMessage())
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(errorResponse.getStatusCode()));
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(UNAUTHORIZED)
    ResponseEntity<ErrorResponse> handleInvalidBearerTokenException(HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(UNAUTHORIZED.value())
                .errorCode(INVALID_TOKEN)
                .message("Provided access token is expired, revoked, malformed or invalid!")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    ResponseEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(FORBIDDEN.value())
                .errorCode(NO_PERMISSION)
                .message("User has no permission to access this resource!")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, FORBIDDEN);
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(FORBIDDEN)
    ResponseEntity<ErrorResponse> handleRefreshTokenException(HttpServletRequest request, RefreshTokenException refreshTokenException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(FORBIDDEN.value())
                .errorCode(refreshTokenException.getErrorCode())
                .message(refreshTokenException.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, FORBIDDEN);
    }

    @ExceptionHandler(TyperException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ResponseEntity<ErrorResponse> handleTyperException(HttpServletRequest request, TyperException typerException) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .errorCode(typerException.getErrorCode())
                .message(typerException.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ResponseEntity<ErrorResponse> handleOtherException(HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .errorCode(OTHER_ERROR)
                .message("A server internal error occurs.")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
    }
}

