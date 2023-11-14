package pl.rafiki.typer.pointrules;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/pointrules")
@Validated
public class PointRulesController {
    private final PointRulesService pointRulesService;

    @Autowired
    public PointRulesController(PointRulesService pointRulesService) {
        this.pointRulesService = pointRulesService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<PointRules> getAllPointRules() {
        return pointRulesService.getAllPointRules();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{pointrulesId}")
    public PointRules getPointRules(@PathVariable(name = "pointrulesId") Long pointRulesId) {
        return pointRulesService.getPointRules(pointRulesId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public void addNewPointRules(@RequestBody @Valid PointRules pointRules) {
        pointRulesService.addNewPointRules(pointRules);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{pointrulesId}")
    public void updatePointRules(@PathVariable(name = "pointrulesId") Long pointrulesId, @RequestBody @Valid PointRules pointRules) {
        pointRulesService.updatePointRules(pointrulesId, pointRules);
    }
}
