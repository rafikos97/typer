import { createReducer, on } from '@ngrx/store';
import { fetchMatchesSuccess, refetchMatchesSuccess } from './match.actions';
import { Matches } from '../../models/match.model';

export type MatchesFetchingStatus = 'INITIAL' | 'FETCHING' | 'READY';

export interface MatchesState {
    matches: Matches | null;
    status: MatchesFetchingStatus;
}

export const initialMatchesState: MatchesState = {
    matches: null,
    status: 'INITIAL',
};

export const matchesReducer = createReducer<MatchesState>(
    initialMatchesState,
    on(fetchMatchesSuccess, refetchMatchesSuccess, (state, { matches }) => ({
        ...state,
        matches,
        status: 'READY',
    })),
);
