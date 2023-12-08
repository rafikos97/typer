package pl.rafiki.typer.match;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinishMatchDTO {
    @NotNull(message = "First team score cannot be null")
    private Integer firstTeamScore;
    @NotNull(message = "Second team score cannot be null")
    private Integer secondTeamScore;
}