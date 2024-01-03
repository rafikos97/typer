import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
    initializeAuthentication,
    login,
    loginSuccess,
    redirectToLogin,
    useExistingAuthentication,
} from './authentication.actions';
import { switchMap, tap } from 'rxjs';
import { AuthenticationService } from '../services/authentication/authentication.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthenticationEffects {
    private readonly actions$ = inject(Actions);
    private readonly authenticationService = inject(AuthenticationService);
    private readonly router = inject(Router);

    readonly login$ = createEffect(() =>
        this.actions$.pipe(
            ofType(login),
            switchMap(({ username, password }) =>
                this.authenticationService.login(username, password),
            ),
            switchMap(({ accessToken, scope, expiresIn, tokenType }) => {
                this.authenticationService.persistAuthentication({
                    accessToken,
                    scope,
                    expiresIn,
                    tokenType,
                });
                return [
                    loginSuccess({ accessToken, expiresIn, tokenType, scope }),
                ];
            }),
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

    readonly redirectToLogin$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(redirectToLogin),
                tap(() => this.router.navigateByUrl('/login')),
            ),
        { dispatch: false },
    );

    readonly redirectToHome$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(useExistingAuthentication),
                tap(() => this.router.navigateByUrl('/main/home')),
            ),
        { dispatch: false },
    );

    readonly initializeAuthentication$ = createEffect(() =>
        this.actions$.pipe(
            ofType(initializeAuthentication),
            switchMap(() => {
                const existingAuthentication =
                    this.authenticationService.retrieveAuthenticationFromStorage();
                if (existingAuthentication) {
                    return [useExistingAuthentication(existingAuthentication)];
                } else {
                    return [redirectToLogin()];
                }
            }),
        ),
    );
}
