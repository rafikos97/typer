import { createReducer, on } from '@ngrx/store';
import { PointRules } from '../../models/point-rules.model';
import {
    fetchPointRulesSuccess,
    refetchPointRulesSuccess,
} from './point-rules.actions';

export type PointRulesFetchingStatus = 'INITIAL' | 'FETCHING' | 'READY';

export interface PointRulesState {
    pointRules: PointRules | null;
    status: PointRulesFetchingStatus;
}

export const initialPointRulesState: PointRulesState = {
    pointRules: null,
    status: 'INITIAL',
};

export const pointRulesReducer = createReducer(
    initialPointRulesState,
    on(
        fetchPointRulesSuccess,
        refetchPointRulesSuccess,
        (state, { pointRules }) => ({
            ...state,
            pointRules,
        }),
    ),
);
