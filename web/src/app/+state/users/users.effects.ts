import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';
import { User, Users } from '../../models/users.model';
import {
    createUser,
    createUserSuccess,
    deleteUser,
    deleteUserSuccess,
    fetchUsers,
    fetchUsersSuccess,
    refetchUsers,
    refetchUsersSuccess,
    updateUser,
    updateUserSuccess,
} from './users.actions';
import { UsersService } from '../../services/users/users.service';

@Injectable()
export class UsersEffects {
    private readonly usersService = inject(UsersService);
    private readonly actions$ = inject(Actions);

    readonly fetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(fetchUsers),
            switchMap(() => this.usersService.getUsers()),
            map((users: Users) => fetchUsersSuccess({ users })),
        ),
    );

    readonly refetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(refetchUsers),
            switchMap(() => this.usersService.getUsers()),
            map((users: Users) => refetchUsersSuccess({ users })),
        ),
    );

    readonly update$ = createEffect(() =>
        this.actions$.pipe(
            ofType(updateUser),
            switchMap(({ user, id }) => this.usersService.updateUser(user, id)),
            map((user: User) => updateUserSuccess({ user })),
        ),
    );

    readonly create$ = createEffect(() =>
        this.actions$.pipe(
            ofType(createUser),
            switchMap(({ user }) => this.usersService.createUser(user)),
            switchMap((user: User) => [
                createUserSuccess({ user }),
                refetchUsers(),
            ]),
        ),
    );

    readonly delete$ = createEffect(() =>
        this.actions$.pipe(
            ofType(deleteUser),
            switchMap(({ id }) => this.usersService.deleteUser(id)),
            switchMap(() => [deleteUserSuccess(), refetchUsers()]),
        ),
    );
}
