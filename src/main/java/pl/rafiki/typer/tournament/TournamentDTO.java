package pl.rafiki.typer.tournament;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {
    private Long id;
    private String tournamentName;
    private String tournamentCode;
    private String pointRulesCode;

    public TournamentDTO(String tournamentName, String tournamentCode, String pointRulesCode) {
        this.tournamentName = tournamentName;
        this.tournamentCode = tournamentCode;
        this.pointRulesCode = pointRulesCode;
    }
}
