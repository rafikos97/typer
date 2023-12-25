import { createAction, props } from '@ngrx/store';
import { User, Users } from '../../models/users.model';

export const fetchUsers = createAction('[User] Fetch Users');

export const fetchUsersSuccess = createAction(
    '[User] Fetch Users Success',
    props<{ users: Users }>(),
);

export const refetchUsers = createAction('[User] Refetch Users');

export const refetchUsersSuccess = createAction(
    '[User] Refetch Users Success',
    props<{ users: Users }>(),
);

export const createUser = createAction(
    '[User] Create User',
    props<{ user: Omit<User, 'id'> }>(),
);

export const createUserSuccess = createAction(
    '[User] Create User Success',
    props<{ user: User }>(),
);

export const updateUser = createAction(
    '[User] Update User',
    props<{ user: User; id: string }>(),
);

export const updateUserSuccess = createAction(
    '[User] Update User Success',
    props<{ user: User }>(),
);

export const deleteUser = createAction(
    '[User] Delete User',
    props<{ id: number }>(),
);

export const deleteUserSuccess = createAction('[User] Delete User Success');
