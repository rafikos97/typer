package pl.rafiki.typer.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rafiki.typer.pointrules.PointRules;

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
public class Tournament {
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

    @NotNull(message = "Tournament name cannot be null")
    @Column(
            name = "tournament_name",
            nullable = false
    )
    private String tournamentName;

    @NotNull(message = "Tournament code cannot be null")
    @Column(
            name = "tournament_code",
            nullable = false
    )
    private String tournamentCode;

    @NotNull(message = "Point rules code cannot be null")
    @Column(
            name = "point_rules_code",
            nullable = false
    )
    private String pointRulesCode;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "pointrules_id",
            referencedColumnName = "pointrules_id",
            foreignKey = @ForeignKey(name = "Fk_tournament_pointrules")
    )
    private PointRules pointRules;

    public Tournament(String tournamentName, String tournamentCode, String pointRulesCode) {
        this.tournamentName = tournamentName;
        this.tournamentCode = tournamentCode;
        this.pointRulesCode = pointRulesCode;
    }

    public String getPointRulesCode() {
        if (pointRulesCode != null && pointRules != null && !pointRulesCode.equals(pointRules.getPointRulesCode())) {
            setPointRulesCode(pointRules.getPointRulesCode());
        }
        return pointRulesCode;
    }
}
