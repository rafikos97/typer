import {
    ChangeDetectionStrategy,
    Component,
    OnInit,
    TrackByFunction,
    inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { Tournament } from './models/tournament.model';
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { selectTournaments } from './+state/tournament/tournament.selectors';
import {
    createTournament,
    fetchTournaments,
} from './+state/tournament/tournament.actions';
import { AsyncPipe, NgFor } from '@angular/common';
import { PointRule } from './models/point-rules.model';
import { selectPointRules } from './+state/point-rules/point-rules.selectors';
import {
    createPointRule,
    fetchPointRules,
} from './+state/point-rules/point-rules.actions';
import { Match } from './models/match.model';
import { createMatch, fetchMatches } from './+state/match/match.actions';
import { selectMatches } from './+state/match/match.selectors';

@Component({
    templateUrl: './admin-panel.component.html',
    styleUrls: ['./admin-panel.component.scss'],
    selector: 'app-admin-panel',
    changeDetection: ChangeDetectionStrategy.OnPush,
    standalone: true,
    imports: [AsyncPipe, NgFor, ReactiveFormsModule],
})
export class AdminPanelComponent implements OnInit {
    private readonly store = inject(Store);

    readonly trackPointRuleCodesByFn: TrackByFunction<PointRule> = (
        _: number,
        pointRule: PointRule,
    ) => pointRule.pointRulesCode;
    readonly trackTournamentsByFn: TrackByFunction<Tournament> = (
        _: number,
        tournament: Tournament,
    ) => tournament.tournamentCode;
    readonly trackMatchByFn: TrackByFunction<Match> = (
        _: number,
        match: Match,
    ) => match.firstTeamName + match.startDateAndTime;

    readonly pointRules$ = this.store.select(selectPointRules);
    readonly tournaments$ = this.store.select(selectTournaments);
    readonly matches$ = this.store.select(selectMatches);

    readonly pointRuleForm = new FormGroup({
        score: new FormControl('', Validators.required),
        winner: new FormControl('', Validators.required),
        pointRulesCode: new FormControl('', Validators.required),
    });
    readonly tournamentForm = new FormGroup({
        tournamentName: new FormControl('', Validators.required),
        tournamentCode: new FormControl('', Validators.required),
        pointRulesCode: new FormControl('', Validators.required),
    });
    readonly matchForm = new FormGroup({
        firstTeamName: new FormControl('', Validators.required),
        secondTeamName: new FormControl('', Validators.required),
        startDateAndTime: new FormControl('', Validators.required),
        firstTeamScore: new FormControl('', Validators.required),
        secondTeamScore: new FormControl('', Validators.required),
        finished: new FormControl('', Validators.required),
        tournamentCode: new FormControl('', Validators.required),
    });

    ngOnInit() {
        this.store.dispatch(fetchPointRules());
        this.store.dispatch(fetchTournaments());
        this.store.dispatch(fetchMatches());
    }

    createPointRule() {
        const pointRuleToCreate: PointRule = {
            winner: Number(this.pointRuleForm.value.winner!),
            score: Number(this.pointRuleForm.value.score),
            pointRulesCode: this.pointRuleForm.value.pointRulesCode!,
        };
        this.store.dispatch(createPointRule({ pointRule: pointRuleToCreate }));
    }

    createTournament() {
        const { tournamentCode, tournamentName, pointRulesCode } = this
            .tournamentForm.value as NonNullable<Tournament>;
        const tournamentToCreate: Tournament = {
            tournamentCode,
            tournamentName,
            pointRulesCode,
        };
        this.store.dispatch(
            createTournament({ tournament: tournamentToCreate }),
        );
    }

    createMatch() {
        const {
            firstTeamName,
            secondTeamName,
            startDateAndTime,
            firstTeamScore,
            secondTeamScore,
            finished,
            tournamentCode,
        } = this.matchForm.value;

        const matchToCreate: Match = {
            firstTeamName: firstTeamName!,
            secondTeamName: secondTeamName!,
            startDateAndTime: new Date(startDateAndTime!).toISOString(),
            firstTeamScore: Number(firstTeamScore),
            secondTeamScore: Number(secondTeamScore),
            finished: !!finished!,
            tournamentCode: tournamentCode!,
        };
        this.store.dispatch(createMatch({ match: matchToCreate }));
    }
}
