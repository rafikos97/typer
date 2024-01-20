import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
    initializeAuthentication,
    login,
    loginSuccess,
    redirectToLogin,
    refreshToken,
    refreshTokenSuccess,
    useExistingAuthentication,
} from './authentication.actions';
import { catchError, of, switchMap, tap, withLatestFrom } from 'rxjs';
import { AuthenticationService } from '../services/authentication/authentication.service';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { selectRefreshToken } from './authentication.selectors';
import { fetchMatches } from 'src/app/+state/match/match.actions';
import { fetchPointRules } from 'src/app/+state/point-rules/point-rules.actions';
import { fetchTournaments } from 'src/app/+state/tournament/tournament.actions';
import { fetchUsers } from 'src/app/+state/users/users.actions';

@Injectable()
export class AuthenticationEffects {
    private readonly actions$ = inject(Actions);
    private readonly authenticationService = inject(AuthenticationService);
    private readonly router = inject(Router);
    private readonly store = inject(Store);

    readonly login$ = createEffect(() =>
        this.actions$.pipe(
            ofType(login),
            switchMap(({ username, password }) =>
                this.authenticationService.login(username, password),
            ),
            switchMap((loginResponse) => {
                this.authenticationService.persistAuthentication(loginResponse);
                return [loginSuccess(loginResponse)];
            }),
        ),
    );

    readonly loginSuccess$ = createEffect(() =>
        this.actions$.pipe(
            ofType(loginSuccess),
            tap(() => {
                this.router.navigateByUrl('/main/home');
            }),
            switchMap(() => [
                fetchPointRules(),
                fetchTournaments(),
                fetchMatches(),
                fetchUsers(),
            ]),
        ),
    );

    readonly refreshToken$ = createEffect(() =>
        this.actions$.pipe(
            ofType(refreshToken),
            withLatestFrom(this.store.select(selectRefreshToken)),
            switchMap(([_, refreshToken]) =>
                this.authenticationService.refreshToken(refreshToken!),
            ),
            switchMap((loginResponse) => {
                this.authenticationService.persistAuthentication(loginResponse);
                return [refreshTokenSuccess(loginResponse)];
            }),
        ),
    );

    readonly redirectToLogin$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(redirectToLogin),
                tap(() => this.router.navigateByUrl('/login')),
            ),
        { dispatch: false },
    );

    readonly redirectToCurrentPath = createEffect(() =>
        this.actions$.pipe(
            ofType(useExistingAuthentication),
            switchMap(() => [
                fetchPointRules(),
                fetchTournaments(),
                fetchMatches(),
                fetchUsers(),
            ]),
            tap(() => {
                const currentPathIsIndex = location.pathname === '/';
                this.router.navigateByUrl(
                    currentPathIsIndex ? '/main/home' : location.pathname,
                );
            }),
        ),
    );

    readonly initializeAuthentication$ = createEffect(() =>
        this.actions$.pipe(
            ofType(initializeAuthentication),
            switchMap(() => {
                const existingAuthentication =
                    this.authenticationService.retrieveAuthenticationFromStorage();
                if (existingAuthentication) {
                    return this.authenticationService.refreshToken(
                        existingAuthentication.refreshToken,
                    );
                }

                throw new Error('No existing authentication found.');
            }),
            switchMap((refreshedAuthentication) => {
                return [useExistingAuthentication(refreshedAuthentication)];
            }),
            catchError(() => of(redirectToLogin())),
        ),
    );
}
