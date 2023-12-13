package pl.rafiki.typer.bet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.user.User;

@Entity(name = "bet")
@Table(name = "bet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bet {
    @Id
    @SequenceGenerator(
            name = "bet_sequence",
            sequenceName = "bet_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bet_sequence"
    )
    @Column(
            name = "bet_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "first_team_score",
            nullable = false
    )
    private Integer firstTeamScore;

    @Column(
            name = "second_team_score",
            nullable = false
    )
    private Integer secondTeamScore;

    @ManyToOne
    @JoinColumn(
            name = "match_id",
            referencedColumnName = "match_id",
            foreignKey = @ForeignKey(name = "Fk_match_bet"),
            updatable = false
    )
    private Match match;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(name = "Fk_user_bet"),
            updatable = false
    )
    private User user;

    @Column(
            name = "finished"
    )
    private boolean finished;

    @Column(
            name = "winner"
    )
    private int winner;

    @Column(
            name = "score"
    )
    private int score;

    @Column(
            name = "total_points"
    )
    private int totalPoints;

    public Bet(int firstTeamScore, int secondTeamScore, Match match, User user, boolean finished, int winner, int score, int totalPoints) {
        this.firstTeamScore = firstTeamScore;
        this.secondTeamScore = secondTeamScore;
        this.match = match;
        this.user = user;
        this.finished = finished;
        this.winner = winner;
        this.score = score;
        this.totalPoints = totalPoints;
    }
}
