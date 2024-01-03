import { AsyncPipe, NgIf } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import {
    Router,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
} from '@angular/router';
import { Store } from '@ngrx/store';
import { selectUserAuthenticated } from './modules/main/modules/authentication/+state/authentication.selectors';
import { initializeAuthentication } from './modules/main/modules/authentication/+state/authentication.actions';

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
    readonly authenticated$ = this.store.select(selectUserAuthenticated);

    ngOnInit() {
        this.store.dispatch(initializeAuthentication());
    }

    get routerActiveClassName() {
        return 'typer-link-active';
    }
}
