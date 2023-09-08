package pl.rafiki.typer.match;

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
    private String firstTeamName;
    private String secondTeamName;
    private LocalDateTime startDateAndTime;
    private Integer firstTeamScore;
    private Integer secondTeamScore;
    private boolean finished;
    private String tournamentCode;
}
