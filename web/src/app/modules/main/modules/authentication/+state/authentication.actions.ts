import { createAction, props } from '@ngrx/store';

export const login = createAction(
    '[Login]',
    props<{ username: string; password: string }>(),
);

export const loginSuccess = createAction(
    '[Login] Success',
    props<{ token: string; expires: number }>(),
);
