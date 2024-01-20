import { createFeatureSelector, createSelector } from '@ngrx/store';
import { ApplicationContextState } from './application-context.reducer';
import { applicationContextFeatureKey } from './application-context.feature-key';

export const selectApplicationContext =
    createFeatureSelector<ApplicationContextState>(
        applicationContextFeatureKey,
    );

export const selectTournamentId = createSelector(
    selectApplicationContext,
    (selectApplicationContext) => selectApplicationContext?.tournamentId,
);
