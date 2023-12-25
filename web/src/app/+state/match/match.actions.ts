import { createAction, props } from '@ngrx/store';
import { Match, Matches } from '../../models/match.model';

export const fetchMatches = createAction('[Match] Fetch Matches');

export const fetchMatchesSuccess = createAction(
    '[Match] Fetch Matches Success',
    props<{ matches: Matches }>(),
);

export const refetchMatches = createAction('[Match] Refetch Matches');

export const refetchMatchesSuccess = createAction(
    '[Match] Refetch Matches Success',
    props<{ matches: Matches }>(),
);

export const createMatch = createAction(
    '[Match] Create Match',
    props<{ match: Match }>(),
);

export const createMatchSuccess = createAction(
    '[Match] Create Match Success',
    props<{ match: Match }>(),
);

export const updateMatch = createAction(
    '[Match] Update Match',
    props<{ match: Match; id: string }>(),
);

export const updateMatchSuccess = createAction(
    '[Match] Update Match Success',
    props<{ match: Match }>(),
);

export const deleteMatch = createAction(
    '[Match] Delete Match',
    props<{ id: number }>(),
);

export const deleteMatchSuccess = createAction('[Match] Delete Match Success');
