import { createAction, props } from '@ngrx/store';
import { UserInformation } from '../models/user-information.model';
import { HttpErrorResponse } from '@angular/common/http';

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

export const updateUserInformationFailure = createAction(
    '[User Panel] Update User Information Failure',
    props<{ error: HttpErrorResponse }>(),
);
