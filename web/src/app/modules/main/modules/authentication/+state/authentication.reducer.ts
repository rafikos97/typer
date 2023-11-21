import { createReducer, on } from '@ngrx/store';
import { loginSuccess } from './authentication.actions';
import { Scope } from '../models/scope.model';

export interface AuthenticationState {
    accessToken: string | null;
    expiresIn: number | null;
    tokenType: string | null;
    scope: Scope | null;
}

export const authenticationFeatureKey = 'authentication';

export const initialAuthenticationState: AuthenticationState = {
    accessToken: null,
    expiresIn: null,
    tokenType: null,
    scope: null,
};

export const authenticationReducer = createReducer(
    initialAuthenticationState,
    on(loginSuccess, (_, { expiresIn, accessToken, tokenType, scope }) => ({
        expiresIn,
        accessToken,
        tokenType,
        scope,
    })),
);
