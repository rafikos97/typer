import { createReducer, on } from '@ngrx/store';
import { fetchTournamentsSuccess } from './tournament.actions';
import { Tournaments } from '../../models/tournament.model';

export type TournamentsFetchingStatus = 'INITIAL' | 'FETCHING' | 'READY';

export interface TournamentsState {
    tournaments: Tournaments | null;
    status: TournamentsFetchingStatus;
}

export const initialTournamentsState: TournamentsState = {
    tournaments: null,
    status: 'INITIAL',
};

export const tournamentsReducer = createReducer(
    initialTournamentsState,
    on(fetchTournamentsSuccess, (state, { tournaments }) => ({
        ...state,
        tournaments,
    })),
);
