import { createAction, props } from '@ngrx/store';

export const loginSuccess = createAction(
    '[Login] Success',
    props<{ token: string; expires: number }>(),
);
