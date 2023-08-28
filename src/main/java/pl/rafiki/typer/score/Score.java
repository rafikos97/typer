package pl.rafiki.typer.score;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rafiki.typer.tournament.Tournament;
import pl.rafiki.typer.user.User;

@Entity(name = "score")
@Table(name = "score")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @Id
    @SequenceGenerator(
            name = "score_sequence",
            sequenceName = "score_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "score_sequence"
    )
    Long id;

    @JsonProperty(value = "userId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "Fk_user_score"),
            updatable = false
    )
    private User user;

    @JsonProperty(value = "tournamentId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "tournament_id",
            referencedColumnName = "tournament_id",
            foreignKey = @ForeignKey(name = "Fk_tournament_score")
    )
    private Tournament tournament;

    @Column(
            name = "winners",
            updatable = false
    )
    private Integer winners;

    @Column(
            name = "scores",
            updatable = false
    )
    private Integer scores;

    @Column(
            name = "totalPoints",
            updatable = false
    )
    private Integer totalPoints;

    public Score(User user, Tournament tournament, Integer winners, Integer scores, Integer totalPoints) {
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
