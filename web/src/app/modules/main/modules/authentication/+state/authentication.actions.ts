import { createAction, props } from '@ngrx/store';
import { Scope } from '../models/scope.model';

export const login = createAction(
    '[Login]',
    props<{ username: string; password: string }>(),
);

export const loginSuccess = createAction(
    '[Login] Success',
    props<{
        accessToken: string;
        expiresIn: number;
        scope: Scope;
        tokenType: string;
    }>(),
);
