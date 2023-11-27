import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';
import { Tournament, Tournaments } from '../../models/tournament.model';
import {
    createTournament,
    createTournamentSuccess,
    deleteTournament,
    deleteTournamentSuccess,
    fetchTournaments,
    fetchTournamentsSuccess,
    updateTournament,
    updateTournamentSuccess,
} from './tournament.actions';
import { TournamentsService } from '../../services/tournaments/tournaments.service';

@Injectable()
export class TournamentsEffects {
    private readonly tournamentsService = inject(TournamentsService);
    private readonly actions$ = inject(Actions);

    readonly fetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(fetchTournaments),
            switchMap(() => this.tournamentsService.getTournaments()),
            map((tournaments: Tournaments) =>
                fetchTournamentsSuccess({ tournaments }),
            ),
        ),
    );

    readonly update$ = createEffect(() =>
        this.actions$.pipe(
            ofType(updateTournament),
            switchMap(({ tournament, id }) =>
                this.tournamentsService.updateTournament(tournament, id),
            ),
            map((tournament: Tournament) =>
                updateTournamentSuccess({ tournament }),
            ),
        ),
    );

    readonly create$ = createEffect(() =>
        this.actions$.pipe(
            ofType(createTournament),
            switchMap(({ tournament }) =>
                this.tournamentsService.createTournament(tournament),
            ),
            map((tournament: Tournament) =>
                createTournamentSuccess({ tournament }),
            ),
        ),
    );

    readonly delete$ = createEffect(() =>
        this.actions$.pipe(
            ofType(deleteTournament),
            switchMap(({ id }) => this.tournamentsService.deleteTournament(id)),
            map(() => deleteTournamentSuccess()),
        ),
    );
}
