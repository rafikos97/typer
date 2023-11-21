import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
    AuthenticationState,
    authenticationFeatureKey,
} from './authentication.reducer';

export const selectAuthentication = createFeatureSelector<AuthenticationState>(
    authenticationFeatureKey,
);

export const selectUserAuthenticated = createSelector(
    selectAuthentication,
    (authentication) => !!authentication?.token,
);
