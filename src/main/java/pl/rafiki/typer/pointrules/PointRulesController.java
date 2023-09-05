package pl.rafiki.typer.pointrules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/pointrules")
public class PointRulesController {
    private final PointRulesService pointRulesService;

    @Autowired
    public PointRulesController(PointRulesService pointRulesService) {
        this.pointRulesService = pointRulesService;
    }

    @GetMapping(path = "/all")
    public List<PointRules> getAllPointRules() {
        return pointRulesService.getAllPointRules();
    }

    @GetMapping(path = "/{pointrulesId}")
    public PointRules getPointRules(@PathVariable(name = "pointrulesId") Long pointRulesId) {
        return pointRulesService.getPointRules(pointRulesId);
    }

    @PostMapping()
    public void addNewPointRules(@RequestBody PointRules pointRules) {
        pointRulesService.addNewPointRules(pointRules);
    }

    @PutMapping(path = "/{pointrulesId}")
    public void updatePointRules(@PathVariable(name = "pointrulesId") Long pointrulesId, @RequestBody PointRules pointRules) {
        pointRulesService.updatePointRules(pointrulesId, pointRules);
    }
}
