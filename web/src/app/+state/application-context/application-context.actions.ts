import { createAction, props } from '@ngrx/store';

export const switchTournamentContext = createAction(
    '[Application Context] Switch Tournament Context',
    props<{ tournamentId: number }>(),
);
