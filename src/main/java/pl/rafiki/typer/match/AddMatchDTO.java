package pl.rafiki.typer.match;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddMatchDTO {
    @NotNull(message = "First team name cannot be null")
    private String firstTeamName;
    @NotNull(message = "Second team name cannot be null")
    private String secondTeamName;
    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDateAndTime;
    @NotNull(message = "Tournament code cannot be null!")
    private String tournamentCode;
}