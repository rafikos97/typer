package pl.rafiki.typer.exceptionhandling;

import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private ErrorCode errorCode;
    private String message;
    private LocalDateTime timestamp;
    private String path;

    public LocalDateTime getTimestamp() {
        return timestamp.truncatedTo(ChronoUnit.SECONDS);
    }
}
