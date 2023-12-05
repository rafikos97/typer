import { AsyncPipe, NgIf } from '@angular/common';
import { Component, ElementRef, OnInit, inject } from '@angular/core';
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

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    standalone: true,
    imports: [RouterLink, RouterLinkActive, RouterOutlet, NgIf, AsyncPipe],
})
export class AppComponent implements OnInit {
    private readonly router = inject(Router);
    private readonly store = inject(Store);
    private readonly elementRef = inject(ElementRef);
    readonly authenticated$ = this.store.select(selectUserAuthenticated);

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

    ngOnInit() {
        this.router.navigateByUrl('/login');
    }

    get routerActiveClassName() {
        return 'typer-link-active';
    }
}
