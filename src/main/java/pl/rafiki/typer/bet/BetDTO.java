package pl.rafiki.typer.bet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BetDTO {
    private Long id;
    private Integer firstTeamScore;
    private Integer secondTeamScore;
    private Long matchId;
}
