package pl.rafiki.typer.pointrules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRulesRepository extends JpaRepository<PointRules, Long> {

    boolean existsByPointRulesCode(String code);
    Optional<PointRules> findPointRulesByPointRulesCode(String code);
}
