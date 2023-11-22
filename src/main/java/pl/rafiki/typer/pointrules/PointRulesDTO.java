package pl.rafiki.typer.pointrules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRulesDTO {
    private Long id;
    private String pointRulesCode;
    private Integer winner;
    private Integer score;
}
