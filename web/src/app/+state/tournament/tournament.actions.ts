import { createAction, props } from '@ngrx/store';
import { Tournament, Tournaments } from '../../models/tournament.model';

export const fetchTournaments = createAction('[Tournament] Fetch Tournaments');

export const fetchTournamentsSuccess = createAction(
    '[Tournament] Fetch Tournaments Success',
    props<{ tournaments: Tournaments }>(),
);

export const refetchTournaments = createAction(
    '[Tournament] Refetch Tournaments',
);

export const refetchTournamentsSuccess = createAction(
    '[Tournament] Refetch Tournaments Success',
    props<{ tournaments: Tournaments }>(),
);

export const createTournament = createAction(
    '[Tournament] Create Tournament',
    props<{ tournament: Tournament }>(),
);

export const createTournamentSuccess = createAction(
    '[Tournament] Create Tournament Success',
    props<{ tournament: Tournament }>(),
);

export const updateTournament = createAction(
    '[Tournament] Update Tournament',
    props<{ tournament: Tournament; id: string }>(),
);

export const updateTournamentSuccess = createAction(
    '[Tournament] Update Tournament Success',
    props<{ tournament: Tournament }>(),
);

export const deleteTournament = createAction(
    '[Tournament] Delete Tournament',
    props<{ id: string }>(),
);

export const deleteTournamentSuccess = createAction(
    '[Tournament] Delete Tournament Success',
);
