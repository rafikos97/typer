import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';
import { Match, Matches } from '../../models/match.model';
import {
    createMatch,
    createMatchSuccess,
    deleteMatch,
    deleteMatchSuccess,
    fetchMatches,
    fetchMatchesSuccess,
    refetchMatches,
    refetchMatchesSuccess,
    updateMatch,
    updateMatchSuccess,
} from './match.actions';
import { MatchesService } from '../../services/matches/matches.service';
import { createPointRuleSuccess } from '../point-rules/point-rules.actions';

@Injectable()
export class MatchesEffects {
    private readonly matchesService = inject(MatchesService);
    private readonly actions$ = inject(Actions);

    readonly fetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(fetchMatches),
            switchMap(() => this.matchesService.getMatches()),
            map((matches: Matches) => fetchMatchesSuccess({ matches })),
        ),
    );

    readonly refetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(refetchMatches, createPointRuleSuccess),
            switchMap(() => this.matchesService.getMatches()),
            map((matches: Matches) => refetchMatchesSuccess({ matches })),
        ),
    );

    readonly update$ = createEffect(() =>
        this.actions$.pipe(
            ofType(updateMatch),
            switchMap(({ match, id }) =>
                this.matchesService.updateMatch(match, id),
            ),
            map((match: Match) => updateMatchSuccess({ match })),
        ),
    );

    readonly create$ = createEffect(() =>
        this.actions$.pipe(
            ofType(createMatch),
            switchMap(({ match }) => this.matchesService.createMatch(match)),
            switchMap((match: Match) => [
                createMatchSuccess({ match }),
                refetchMatches(),
            ]),
        ),
    );

    readonly delete$ = createEffect(() =>
        this.actions$.pipe(
            ofType(deleteMatch),
            switchMap(({ id }) => this.matchesService.deleteMatch(id)),
            map(() => deleteMatchSuccess()),
        ),
    );
}
