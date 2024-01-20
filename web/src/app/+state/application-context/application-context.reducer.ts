import { createReducer, on } from '@ngrx/store';
import { switchTournamentContext } from './application-context.actions';

export interface ApplicationContextState {
    tournamentId: number | null;
}

export const initialApplicationContextState: ApplicationContextState = {
    tournamentId: null,
};

export const applicationContextReducer = createReducer<ApplicationContextState>(
    initialApplicationContextState,
    on(switchTournamentContext, (_, { tournamentId }) => ({
        tournamentId,
    })),
);
