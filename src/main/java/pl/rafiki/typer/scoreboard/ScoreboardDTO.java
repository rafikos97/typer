package pl.rafiki.typer.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreboardDTO {
    private Long id;
    private Integer winners;
    private Integer scores;
    private Integer totalPoints;
    private String userFirstName;
    private String userLastName;
}
