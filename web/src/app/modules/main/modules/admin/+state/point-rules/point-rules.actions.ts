import { createAction, props } from '@ngrx/store';
import { PointRule, PointRules } from '../../models/point-rules.model';

export const fetchPointRules = createAction('[Point Rules] Fetch Point Rules');

export const fetchPointRulesSuccess = createAction(
    '[Point Rules] Fetch Point Rules Success',
    props<{ pointRules: PointRules }>(),
);

export const refetchPointRules = createAction(
    '[Point Rules] Refetch Point Rules',
);

export const refetchPointRulesSuccess = createAction(
    '[Point Rules] Refetch Point Rules Success',
    props<{ pointRules: PointRules }>(),
);

export const createPointRule = createAction(
    '[Point Rules] Create Point Rule',
    props<{ pointRule: PointRule }>(),
);

export const createPointRuleSuccess = createAction(
    '[Point Rules] Create Point Rule Success',
    props<{ pointRule: PointRule }>(),
);

export const updatePointRule = createAction(
    '[Point Rules] Update Point Rules',
    props<{ pointRule: PointRule; id: string }>(),
);

export const updatePointRuleSuccess = createAction(
    '[Point Rules] Update Point Rule Success',
    props<{ pointRule: PointRule }>(),
);

export const deletePointRule = createAction(
    '[Point Rules] Delete Point Rule',
    props<{ id: string }>(),
);

export const deletePointRuleSuccess = createAction(
    '[Point Rules] Delete Point Rule Success',
);
