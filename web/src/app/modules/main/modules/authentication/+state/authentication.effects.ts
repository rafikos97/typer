import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
    login,
    loginSuccess,
    refreshToken,
    refreshTokenSuccess,
} from './authentication.actions';
import { switchMap, tap, withLatestFrom } from 'rxjs';
import { AuthenticationService } from '../services/authentication/authentication.service';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { selectRefreshToken } from './authentication.selectors';

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
            switchMap(
                ({
                    accessToken,
                    scope,
                    expiresIn,
                    tokenType,
                    refreshToken,
                }) => [
                    loginSuccess({
                        accessToken,
                        expiresIn,
                        tokenType,
                        scope,
                        refreshToken,
                    }),
                ],
            ),
        ),
    );

    readonly loginSuccess$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(loginSuccess),
                tap(() => {
                    this.router.navigateByUrl('/main/home');
                }),
            ),
        { dispatch: false },
    );

    readonly refreshToken$ = createEffect(() =>
        this.actions$.pipe(
            ofType(refreshToken),
            withLatestFrom(this.store.select(selectRefreshToken)),
            switchMap(([_, refreshToken]) =>
                this.authenticationService.refreshToken(refreshToken!),
            ),
            switchMap(
                ({
                    accessToken,
                    scope,
                    expiresIn,
                    tokenType,
                    refreshToken,
                }) => [
                    refreshTokenSuccess({
                        accessToken,
                        expiresIn,
                        tokenType,
                        scope,
                        refreshToken,
                    }),
                ],
            ),
        ),
    );
}
