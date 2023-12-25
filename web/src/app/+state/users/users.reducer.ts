import { createReducer, on } from '@ngrx/store';
import { fetchUsersSuccess, refetchUsersSuccess } from './users.actions';
import { Users } from '../../models/users.model';

export type UsersFetchingStatus = 'INITIAL' | 'FETCHING' | 'READY';

export interface UsersState {
    users: Users | null;
    status: UsersFetchingStatus;
}

export const initialUsersState: UsersState = {
    users: null,
    status: 'INITIAL',
};

export const usersReducer = createReducer<UsersState>(
    initialUsersState,
    on(fetchUsersSuccess, refetchUsersSuccess, (state, { users }) => ({
        ...state,
        users,
        status: 'READY',
    })),
);
