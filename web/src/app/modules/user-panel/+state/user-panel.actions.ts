import { createAction, props } from '@ngrx/store';
import { UserInformation } from '../models/user-information.model';

export const fetchUserInformation = createAction(
    '[User Panel] Fetch User Information',
);

export const fetchUserInformationSuccess = createAction(
    '[User Panel] Fetch User Information',
    props<{ userInformation: UserInformation }>(),
);
