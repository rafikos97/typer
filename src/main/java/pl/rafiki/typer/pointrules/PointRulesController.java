package pl.rafiki.typer.pointrules;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/typer/pointrules")
@Validated
@Tag(name = "PointRules", description = "Point rules management APIs")
public class PointRulesController {
    private final PointRulesService pointRulesService;

    @Autowired
    public PointRulesController(PointRulesService pointRulesService) {
        this.pointRulesService = pointRulesService;
    }

    @Operation(
            summary = "Get all defined point rules.",
            description = "Method to get all defined point rules."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    public List<PointRulesDTO> getAllPointRules() {
        return pointRulesService.getAllPointRules();
    }

    @Operation(
            summary = "Get point rules by pointrulesId.",
            description = "Method to get specific point rules by pointrulesId."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{pointrulesId}")
    public PointRulesDTO getPointRules(@PathVariable(name = "pointrulesId") Long pointRulesId) {
        return pointRulesService.getPointRules(pointRulesId);
    }

    @Operation(
            summary = "Add new point rules.",
            description = "Method for adding new point rules."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/add")
    public void addNewPointRules(@RequestBody @Valid PointRulesDTO pointRulesDTO) {
        pointRulesService.addNewPointRules(pointRulesDTO);
    }

    @Operation(
            summary = "Update point rules by pointrulesId.",
            description = "Method to update specific point rules."
    )
    @Parameter(name = "Authorization", description = "Bearer token", required = true, in = ParameterIn.HEADER)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{pointrulesId}")
    public PointRulesDTO updatePointRules(@PathVariable(name = "pointrulesId") Long pointrulesId, @RequestBody @Valid PointRulesDTO pointRulesDTO) {
        return pointRulesService.updatePointRules(pointrulesId, pointRulesDTO);
    }
}
