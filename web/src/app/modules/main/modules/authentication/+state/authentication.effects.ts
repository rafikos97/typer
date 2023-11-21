import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { login, loginSuccess } from './authentication.actions';
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
            switchMap((loginResponse) => {
                const token = loginResponse['access_token'];
                const expires = loginResponse['expires_in'];
                return [loginSuccess({ token, expires })];
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
}
