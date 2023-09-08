package pl.rafiki.typer.tournament;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {
    private String tournamentName;
    private String tournamentCode;
    private String pointRulesCode;
}
