package pl.rafiki.typer.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rafiki.typer.tournament.Tournament;

import java.time.LocalDateTime;

@Entity(name = "match")
@Table(name = "match")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
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

    @NotNull(message = "First team name cannot be null")
    @Column(
            name = "first_team_name",
            nullable = false
    )
    private String firstTeamName;

    @NotNull(message = "Second team name cannot be null")
    @Column(
            name = "second_team_name",
            nullable = false
    )
    private String secondTeamName;

    @NotNull(message = "Start date cannot be null")
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

    @NotNull(message = "Tournament code cannot be null!")
    @Column(
            name = "tournament_code",
            nullable = false
    )
    private String tournamentCode;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "tournament_id",
            referencedColumnName = "tournament_id",
            foreignKey = @ForeignKey(name = "Fk_tournament_match")
    )
    private Tournament tournament;

    public Match(String firstTeamName, String secondTeamName, LocalDateTime startDateAndTime, int firstTeamScore, int secondTeamScore, boolean finished, String tournamentCode) {
        this.firstTeamName = firstTeamName;
        this.secondTeamName = secondTeamName;
        this.startDateAndTime = startDateAndTime;
        this.firstTeamScore = firstTeamScore;
        this.secondTeamScore = secondTeamScore;
        this.finished = finished;
        this.tournamentCode = tournamentCode;
    }

    public String getTournamentCode() {
        if (tournamentCode != null && tournament != null && !tournamentCode.equals(tournament.getTournamentCode())) {
            setTournamentCode(tournament.getTournamentCode());
        }
        return tournamentCode;
    }
}
