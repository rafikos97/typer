import { createFeatureSelector, createSelector } from '@ngrx/store';
import { usersFeatureKey } from './users.feature-key';
import { UsersState } from './users.reducer';

export const selectUsersState =
    createFeatureSelector<UsersState>(usersFeatureKey);

export const selectUsers = createSelector(
    selectUsersState,
    (userState) => userState.users,
);

export const selectUsersFetchStatus = createSelector(
    selectUsersState,
    (userState) => userState.status,
);
