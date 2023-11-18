import { createReducer, on } from '@ngrx/store';
import { loginSuccess } from './authentication.actions';

export interface AuthenticationState {
    token: string;
    expires: number;
}

export const authenticationFeatureKey = 'authentication';

export const initialAuthenticationState: AuthenticationState = {
    expires: 0,
    token: '',
};

export const authenticationReducer = createReducer(
    initialAuthenticationState,
    on(loginSuccess, (state, { expires, token }) => ({ expires, token })),
);
