import { AfterViewInit, Component, ElementRef, inject } from '@angular/core';
import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import {
    Router,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
} from '@angular/router';
import { Store } from '@ngrx/store';
import { selectUserAuthenticated } from './modules/main/modules/authentication/+state/authentication.selectors';
import { auditTime, filter, fromEvent, merge, withLatestFrom } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { refreshToken } from './modules/main/modules/authentication/+state/authentication.actions';
import { selectTournaments } from './+state/tournament/tournament.selectors';
import { switchTournamentContext } from './+state/application-context/application-context.actions';
import { initializeAuthentication } from './modules/main/modules/authentication/+state/authentication.actions';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    standalone: true,
    imports: [
        RouterLink,
        RouterLinkActive,
        RouterOutlet,
        NgIf,
        NgFor,
        AsyncPipe,
    ],
})
export class AppComponent implements AfterViewInit {
    private readonly router = inject(Router);
    private readonly store = inject(Store);
    private readonly elementRef = inject(ElementRef);
    readonly authenticated$ = this.store.select(selectUserAuthenticated);
    readonly tournaments$ = this.store.select(selectTournaments);
    readonly tokenRefreshSubscription = merge(
        fromEvent(this.elementRef.nativeElement, 'mousemove'),
        fromEvent(this.elementRef.nativeElement, 'keydown'),
    )
        .pipe(
            takeUntilDestroyed(),
            auditTime(60000),
            withLatestFrom(this.authenticated$),
            filter(([_, authenticated]) => authenticated),
        )
        .subscribe(() => this.store.dispatch(refreshToken()));

    ngAfterViewInit() {
        this.store.dispatch(initializeAuthentication());
    }

    changeTournamentContext({ target }: Event) {
        this.store.dispatch(
            switchTournamentContext({
                tournamentCode: (<HTMLSelectElement>target).value!,
            }),
        );
    }

    get routerActiveClassName() {
        return 'typer-link-active';
    }
}
