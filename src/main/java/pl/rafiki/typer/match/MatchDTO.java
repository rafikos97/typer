package pl.rafiki.typer.match;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class MatchDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotNull(message = "First team name cannot be null")
    private String firstTeamName;
    @NotNull(message = "Second team name cannot be null")
    private String secondTeamName;
    @NotNull(message = "Start date cannot be null")
    private LocalDateTime startDateAndTime;
    private Integer firstTeamScore;
    private Integer secondTeamScore;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean finished;
    @NotNull(message = "Tournament code cannot be null!")
    private String tournamentCode;

    public MatchDTO(String firstTeamName, String secondTeamName, LocalDateTime startDateAndTime, Integer firstTeamScore, Integer secondTeamScore, boolean finished, String tournamentCode) {
        this.firstTeamName = firstTeamName;
        this.secondTeamName = secondTeamName;
        this.startDateAndTime = startDateAndTime;
        this.firstTeamScore = firstTeamScore;
        this.secondTeamScore = secondTeamScore;
        this.finished = finished;
        this.tournamentCode = tournamentCode;
    }
}
