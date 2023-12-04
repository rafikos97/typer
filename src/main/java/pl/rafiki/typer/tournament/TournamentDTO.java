package pl.rafiki.typer.tournament;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotNull(message = "Tournament name cannot be null")
    private String tournamentName;
    @NotNull(message = "Tournament code cannot be null")
    private String tournamentCode;
    @NotNull(message = "Point rules code cannot be null")
    private String pointRulesCode;

    public TournamentDTO(String tournamentName, String tournamentCode, String pointRulesCode) {
        this.tournamentName = tournamentName;
        this.tournamentCode = tournamentCode;
        this.pointRulesCode = pointRulesCode;
    }
}
