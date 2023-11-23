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
    private Long id;
    private String firstTeamName;
    private String secondTeamName;
    private LocalDateTime startDateAndTime;
    private Integer firstTeamScore;
    private Integer secondTeamScore;
    private boolean finished;
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
