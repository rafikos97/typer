package pl.rafiki.typer.utils;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ErrorResponse {
    private int statusCode;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
}
