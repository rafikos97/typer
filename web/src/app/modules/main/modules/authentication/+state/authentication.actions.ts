import { createAction, props } from '@ngrx/store';
import { Scope } from '../models/scope.model';
import { LoginResponse } from '../models/login-response.model';

export const login = createAction(
    '[Authentication]',
    props<{ username: string; password: string }>(),
);

export const loginSuccess = createAction(
    '[Authentication] Success',
    props<{
        accessToken: string;
        expiresIn: number;
        scope: Scope;
        tokenType: string;
    }>(),
);

export const initializeAuthentication = createAction(
    '[Authentication] Initialize Authentication',
);

export const useExistingAuthentication = createAction(
    '[Authentication] Use Existing Authentication',
    props<{ payload: LoginResponse }>(),
);

export const redirectToLogin = createAction(
    '[Authentication] Redirect To Login',
);
