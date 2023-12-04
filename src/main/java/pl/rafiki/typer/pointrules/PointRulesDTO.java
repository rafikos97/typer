package pl.rafiki.typer.pointrules;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRulesDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotNull(message = "Point rules code cannot be null")
    private String pointRulesCode;
    @NotNull(message = "Winner cannot be null")
    private Integer winner;
    @NotNull(message = "Score cannot be null")
    private Integer score;

    public PointRulesDTO(String pointRulesCode, Integer winner, Integer score) {
        this.pointRulesCode = pointRulesCode;
        this.winner = winner;
        this.score = score;
    }
}
