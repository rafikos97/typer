import { createFeatureSelector, createSelector } from '@ngrx/store';
import { matchesFeatureKey } from './match.feature-key';
import { MatchesState } from './match.reducer';

export const selectMatchesState =
    createFeatureSelector<MatchesState>(matchesFeatureKey);

export const selectMatches = createSelector(
    selectMatchesState,
    (matchesState) => matchesState.matches,
);

export const selectMatchesFetchStatus = createSelector(
    selectMatchesState,
    (matchesState) => matchesState.status,
);
