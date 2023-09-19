package pl.rafiki.typer.bet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetDTO {
    private Long betId;
    private Integer firstTeamScore;
    private Integer secondTeamScore;
    private Long matchId;

    public BetDTO(Bet bet) {
        this.betId = bet.getId();
        this.firstTeamScore = bet.getFirstTeamScore();
        this.secondTeamScore = bet.getSecondTeamScore();
        this.matchId = bet.getMatch().getId();
    }
}
