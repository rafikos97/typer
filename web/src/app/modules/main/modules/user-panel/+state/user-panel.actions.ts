import { createAction, props } from '@ngrx/store';
import { UserInformation } from '../models/user-information.model';
import { UserPassword } from '../models/user-password.model';

export const fetchUserInformation = createAction(
    '[User Panel] Fetch User Information',
);

export const fetchUserInformationSuccess = createAction(
    '[User Panel] Fetch User Information Success',
    props<{ userInformation: UserInformation }>(),
);

export const updateUserInformation = createAction(
    '[User Panel] Update User Information',
    props<{ userInformation: UserInformation }>(),
);

export const updateUserInformationSuccess = createAction(
    '[User Panel] Update User Information Success',
    props<{ userInformation: UserInformation }>(),
);

export const updateUserPassword = createAction(
    '[User Panel] Update User Password',
    props<{ userPassword: UserPassword }>(),
);

export const updateUserPasswordSuccess = createAction(
    '[User Panel] Update User Password Success',
);
