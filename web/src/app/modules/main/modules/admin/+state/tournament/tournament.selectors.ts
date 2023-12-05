import { createFeatureSelector, createSelector } from '@ngrx/store';
import { tournamentsFeatureKey } from './tournament.feature-key';
import { TournamentsState } from './tournament.reducer';

export const selectTournamentsState = createFeatureSelector<TournamentsState>(
    tournamentsFeatureKey,
);

export const selectTournaments = createSelector(
    selectTournamentsState,
    (tournamentState) => tournamentState.tournaments,
);

export const selectTournamentsFetchStatus = createSelector(
    selectTournamentsState,
    (tournamentState) => tournamentState.status,
);
