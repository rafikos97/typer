import { NgFor, NgIf } from '@angular/common';
import { Component, ChangeDetectionStrategy } from '@angular/core';

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
                <tr *ngFor="let scoreboard of scoreboards">
                    <td [textContent]="scoreboard.id"></td>
                    <td [textContent]="scoreboard.winners"></td>
                    <td [textContent]="scoreboard.scores"></td>
                    <td [textContent]="scoreboard.totalPoints"></td>
                    <td [textContent]="scoreboard.userId"></td>
                    <td [textContent]="scoreboard.tournamentId"></td>
                </tr>
                <tr *ngIf="!scoreboards?.length">
                    <td colspan="6">No data.</td>
                </tr>
            </tbody>
        </table>
    `,
    imports: [NgIf, NgFor],
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
    scoreboards: Scoreboards = [
        {
            id: 1,
            winners: 2,
            scores: 3,
            totalPoints: 4,
            userId: 5,
            tournamentId: 6,
        },
        {
            id: 1,
            winners: 2,
            scores: 3,
            totalPoints: 4,
            userId: 5,
            tournamentId: 6,
        },
    ];
}
