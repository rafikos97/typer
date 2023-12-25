import { createAction, props } from '@ngrx/store';
import { LoginResponse } from '../models/login-response.model';

export const login = createAction(
    '[Login]',
    props<{ username: string; password: string }>(),
);

export const loginSuccess = createAction(
    '[Login] Success',
    props<LoginResponse>(),
);

export const refreshToken = createAction('[Login] Refresh Token');

export const refreshTokenSuccess = createAction(
    '[Login] Refresh Token Success',
    props<LoginResponse>(),
);
