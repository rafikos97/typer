import { createReducer, on } from '@ngrx/store';
import { switchTournamentContext } from './application-context.actions';

export interface ApplicationContextState {
    tournamentCode: string | null;
}

export const initialApplicationContextState: ApplicationContextState = {
    tournamentCode: null,
};

export const applicationContextReducer = createReducer<ApplicationContextState>(
    initialApplicationContextState,
    on(switchTournamentContext, (_, { tournamentCode }) => ({
        tournamentCode,
    })),
);
