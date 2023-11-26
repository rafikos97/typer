package pl.rafiki.typer.bet;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetDTO {
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;
    private Long userId;
    @NotNull(message = "First team score cannot be null")
    private Long userId;
    @NotNull(message = "First team score cannot be null")
    private Integer firstTeamScore;
    @NotNull(message = "Second team score cannot be null")
    @NotNull(message = "Second team score cannot be null")
    private Integer secondTeamScore;
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long matchId;

    public BetDTO(Long userId, Integer firstTeamScore, Integer secondTeamScore, Long matchId) {
        this.userId = userId;
        this.firstTeamScore = firstTeamScore;
        this.secondTeamScore = secondTeamScore;
        this.matchId = matchId;
    }

    public BetDTO(Long userId, Integer firstTeamScore, Integer secondTeamScore, Long matchId) {
        this.userId = userId;
        this.firstTeamScore = firstTeamScore;
        this.secondTeamScore = secondTeamScore;
        this.matchId = matchId;
    }
}
