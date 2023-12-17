package pl.rafiki.typer.bet;

<<<<<<< HEAD
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
=======
>>>>>>> 34abc83 (Moved validation for null values from Bet class to DTO so it is possible to use DTO in POST and PUT methods.)
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
<<<<<<< HEAD
    @Schema(accessMode = AccessMode.READ_ONLY)
=======
>>>>>>> 34abc83 (Moved validation for null values from Bet class to DTO so it is possible to use DTO in POST and PUT methods.)
    private Long userId;
    @NotNull(message = "First team score cannot be null")
    private Integer firstTeamScore;
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
}
