import { createReducer, on } from '@ngrx/store';
import { loginSuccess, refreshTokenSuccess } from './authentication.actions';
import { Scope } from '../models/scope.model';

export interface AuthenticationState {
    accessToken: string | null;
    expiresIn: number | null;
    tokenType: string | null;
    scope: Scope | null;
    refreshToken: string | null;
}

export const authenticationFeatureKey = 'authentication';

export const initialAuthenticationState: AuthenticationState = {
    accessToken: null,
    expiresIn: null,
    tokenType: null,
    scope: null,
    refreshToken: null,
};

export const authenticationReducer = createReducer(
    initialAuthenticationState,
    on(
        loginSuccess,
        refreshTokenSuccess,
        (_, { expiresIn, accessToken, tokenType, scope, refreshToken }) => ({
            expiresIn,
            accessToken,
            tokenType,
            scope,
            refreshToken,
        }),
    ),
);
