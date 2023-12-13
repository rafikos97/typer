package pl.rafiki.typer.tournament;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rafiki.typer.pointrules.PointRules;

import java.io.Serializable;

@Entity(name = "tournament")
@Table(
        name = "tournament",
        uniqueConstraints = {
                @UniqueConstraint(name = "tournament_code_unique", columnNames = "tournament_code"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tournament implements Serializable {
    @Id
    @SequenceGenerator(
            name = "tournament_sequence",
            sequenceName = "tournament_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tournament_sequence"
    )
    @Column(
            name = "tournament_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "tournament_name",
            nullable = false
    )
    private String tournamentName;

    @Column(
            name = "tournament_code",
            nullable = false
    )
    private String tournamentCode;

    @ManyToOne
    @JoinColumn(
            name = "pointrules_id",
            referencedColumnName = "pointrules_id",
            foreignKey = @ForeignKey(name = "Fk_tournament_pointrules")
    )
    private PointRules pointRules;

    public Tournament(String tournamentName, String tournamentCode) {
        this.tournamentName = tournamentName;
        this.tournamentCode = tournamentCode;
    }
}
