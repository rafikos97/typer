import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import {
    Component,
    ChangeDetectionStrategy,
    inject,
    InjectionToken,
    OnInit,
} from '@angular/core';
import { Store } from '@ngrx/store';
import {
    Observable,
    first,
    shareReplay,
    switchMap,
    withLatestFrom,
} from 'rxjs';
import { selectTournamentId } from 'src/app/+state/application-context/application-context.selectors';

interface ScoreboardDTO {
    id: number;
    winners: number;
    scores: number;
    totalPoints: number;
    userId: number;
    tournamentId: number;
}

type Scoreboards = Array<ScoreboardDTO>;

@Component({
    selector: 'app-dashboard',
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: `
        <table>
            <thead>
                <th>ID</th>
                <th>Winners</th>
                <th>Scores</th>
                <th>Total Points</th>
                <th>User ID</th>
                <th>Tournament ID</th>
            </thead>
            <tbody>
                <tr *ngFor="let scoreboard of scoreboards$ | async">
                    <td [textContent]="scoreboard.id"></td>
                    <td [textContent]="scoreboard.winners"></td>
                    <td [textContent]="scoreboard.scores"></td>
                    <td [textContent]="scoreboard.totalPoints"></td>
                    <td [textContent]="scoreboard.userId"></td>
                    <td [textContent]="scoreboard.tournamentId"></td>
                </tr>
                <tr *ngIf="!(scoreboards$ | async)?.length">
                    <td colspan="6">No data.</td>
                </tr>
            </tbody>
        </table>
    `,
    imports: [NgIf, NgFor, AsyncPipe],
    styles: [
        `
            th {
                padding: 4px 8px;
            }

            td {
                border: 1px solid;
                padding: 2px 4px;
            }
        `,
    ],
})
export class DashboardComponent {
    private readonly store = inject(Store);
    private readonly httpClient = inject(HttpClient);

    readonly scoreboards$: Observable<Scoreboards> = this.store
        .select(selectTournamentId)
        .pipe(
            first((tournamentId) => tournamentId !== null),
            switchMap((tournamentId) => {
                return this.httpClient.get<Scoreboards>(
                    `/typer/scoreboard/${tournamentId}`,
                );
            }),
            shareReplay(1),
        );

    // ngOnInit(): void {
    //     this.store.select(selectTournamentId).subscribe(())
    //     this.httpClient.get('/typer/scoreboard' + tournamenetId).
    //     throw new Error('Method not implemented.');
    // }
}
