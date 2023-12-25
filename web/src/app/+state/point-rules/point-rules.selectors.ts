import { createFeatureSelector, createSelector } from '@ngrx/store';
import { pointRulesFeatureKey } from './point-rules.feature-key';
import { PointRulesState } from './point-rules.reducer';

export const selectPointRulesState =
    createFeatureSelector<PointRulesState>(pointRulesFeatureKey);

export const selectPointRules = createSelector(
    selectPointRulesState,
    (pointRulesState) => pointRulesState.pointRules,
);

export const selectPointRulesFetchStatus = createSelector(
    selectPointRulesState,
    (pointRulesState) => pointRulesState.status,
);
