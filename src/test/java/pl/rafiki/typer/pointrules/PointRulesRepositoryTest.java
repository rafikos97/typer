package pl.rafiki.typer.pointrules;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PointRulesRepositoryTest {

    @Autowired
    private PointRulesRepository underTest;

    @Test
    void findPointRulesByPointrulesCode() {
        // given
        String pointrulesCode = "pointRulesTestCode";
        PointRules pointRules = new PointRules(
                pointrulesCode,
                1,
                1
        );

        underTest.save(pointRules);

        // when
        Optional<PointRules> pointRulesOptional = underTest.findPointRulesByPointRulesCode(pointrulesCode);

        // then
        assertThat(pointRulesOptional).hasValue(pointRules);
    }

    @Test
    void existsByPointrulesCode() {
        // given
        String pointrulesCode = "pointRulesTestCode";
        PointRules pointRules = new PointRules(
                pointrulesCode,
                1,
                1
        );

        underTest.save(pointRules);

        // when
        boolean expected = underTest.existsByPointRulesCode(pointrulesCode);

        // then
        assertThat(expected).isTrue();
    }
}
