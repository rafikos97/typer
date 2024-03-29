package pl.rafiki.typer.pointrules;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "pointrules")
@Table(
        name = "pointrules",
        uniqueConstraints = {
                @UniqueConstraint(name = "pointrules_code_unique", columnNames = "pointrules_code")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointRules implements Serializable {
    @Id
    @SequenceGenerator(
            name = "pointrules_sequence",
            sequenceName = "pointrules_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pointrules_sequence"
    )
    @Column(
            name = "pointrules_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "pointrules_code",
            nullable = false
    )
    private String pointRulesCode;


    @Column(
            name = "winner",
            nullable = false
    )
    private Integer winner;

    @Column(
            name = "score",
            nullable = false
    )
    private Integer score;

    public PointRules(String pointRulesCode, Integer winner, Integer score) {
        this.pointRulesCode = pointRulesCode;
        this.winner = winner;
        this.score = score;
    }
}
