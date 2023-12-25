import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';
import { PointRulesService } from '../../services/point-rules/point-rules.service';
import {
    createPointRule,
    createPointRuleSuccess,
    deletePointRule,
    deletePointRuleSuccess,
    fetchPointRules,
    fetchPointRulesSuccess,
    refetchPointRules,
    refetchPointRulesSuccess,
    updatePointRuleSuccess,
} from './point-rules.actions';
import { PointRule, PointRules } from '../../models/point-rules.model';

@Injectable()
export class PointRulesEffects {
    private readonly pointRulesService = inject(PointRulesService);
    private readonly actions$ = inject(Actions);

    readonly fetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(fetchPointRules),
            switchMap(() => this.pointRulesService.getPointRules()),
            map((pointRules: PointRules) =>
                fetchPointRulesSuccess({ pointRules }),
            ),
        ),
    );

    readonly refetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(refetchPointRules),
            switchMap(() => this.pointRulesService.getPointRules()),
            map((pointRules: PointRules) =>
                refetchPointRulesSuccess({ pointRules }),
            ),
        ),
    );

    readonly update$ = createEffect(() =>
        this.actions$.pipe(
            ofType(),
            switchMap(({ pointRule, id }) =>
                this.pointRulesService.updatePointRule(pointRule, id),
            ),
            map((pointRule: PointRule) =>
                updatePointRuleSuccess({ pointRule }),
            ),
        ),
    );

    readonly create$ = createEffect(() =>
        this.actions$.pipe(
            ofType(createPointRule),
            switchMap(({ pointRule }) =>
                this.pointRulesService.createPointRule(pointRule),
            ),
            switchMap((pointRule: PointRule) => [
                createPointRuleSuccess({ pointRule }),
                refetchPointRules(),
            ]),
        ),
    );

    readonly delete$ = createEffect(() =>
        this.actions$.pipe(
            ofType(deletePointRule),
            switchMap(({ id }) => this.pointRulesService.deletePointRule(id)),
            switchMap(() => [deletePointRuleSuccess(), refetchPointRules()]),
        ),
    );
}
