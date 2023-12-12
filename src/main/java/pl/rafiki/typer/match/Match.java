package pl.rafiki.typer.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rafiki.typer.tournament.Tournament;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name = "match")
@Table(name = "match")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match implements Serializable {
    @Id
    @SequenceGenerator(
            name = "match_sequence",
            sequenceName = "match_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "match_sequence"
    )
    @Column(
            name = "match_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "first_team_name",
            nullable = false
    )
    private String firstTeamName;

    @Column(
            name = "second_team_name",
            nullable = false
    )
    private String secondTeamName;

    @Column(
            name = "start_date_time",
            nullable = false
    )
    private LocalDateTime startDateAndTime;

    @Column(name = "first_team_score")
    private Integer firstTeamScore;

    @Column(name = "second_team_score")
    private Integer secondTeamScore;

    @Column(name = "finished")
    private boolean finished;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "tournament_id",
            referencedColumnName = "tournament_id",
            foreignKey = @ForeignKey(name = "Fk_tournament_match")
    )
    private Tournament tournament;

    public Match(String firstTeamName, String secondTeamName, LocalDateTime startDateAndTime, int firstTeamScore, int secondTeamScore, boolean finished) {
        this.firstTeamName = firstTeamName;
        this.secondTeamName = secondTeamName;
        this.startDateAndTime = startDateAndTime;
        this.firstTeamScore = firstTeamScore;
        this.secondTeamScore = secondTeamScore;
        this.finished = finished;
    }
}
