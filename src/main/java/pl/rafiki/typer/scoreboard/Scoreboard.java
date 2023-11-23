package pl.rafiki.typer.scoreboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.user.User;

@Entity(name = "scoreboard")
@Table(name = "scoreboard")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scoreboard {
    @Id
    @SequenceGenerator(
            name = "scoreboard_sequence",
            sequenceName = "scoreboard_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "scoreboard_sequence"
    )
    @Column(
            name = "scoreboard_id",
            updatable = false
    )
    private Long id;

    @JsonProperty(value = "userId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "Fk_user_scoreboard"),
            updatable = false
    )
    private User user;

    @JsonProperty(value = "tournamentId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "tournament_id",
            referencedColumnName = "tournament_id",
            foreignKey = @ForeignKey(name = "Fk_tournament_scoreboard")
    )
    private Tournament tournament;

    @Column(
            name = "winners"
    )
    private Integer winners;

    @Column(
            name = "scores"
    )
    private Integer scores;

    @Column(
            name = "totalPoints"
    )
    private Integer totalPoints;

    public Scoreboard(User user, Tournament tournament, Integer winners, Integer scores, Integer totalPoints) {
        this.user = user;
        this.tournament = tournament;
        this.winners = winners;
        this.scores = scores;
        this.totalPoints = totalPoints;
    }

    public Long getUser() {
        return user.getId();
    }

    public Long getTournament() {
        return tournament.getId();
    }
}
