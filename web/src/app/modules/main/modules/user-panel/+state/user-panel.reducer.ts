import { createReducer, on } from '@ngrx/store';
import {
    fetchUserInformationSuccess,
    updateUserInformationFailure,
    updateUserInformationSuccess,
} from './user-panel.actions';
import { HttpErrorResponse } from '@angular/common/http';

export interface UserPanelState {
    firstName: string;
    lastName: string;
    email: string;
    id: string;
    username: string;
    error?: HttpErrorResponse;
}

export const initialUserPanelState: UserPanelState = {
    firstName: '',
    lastName: '',
    email: '',
    id: '',
    username: '',
    error: undefined,
};

export const userPanelReducer = createReducer(
    initialUserPanelState,
    on(
        fetchUserInformationSuccess,
        updateUserInformationSuccess,
        (state, { userInformation }) => ({
            ...state,
            ...userInformation,
        }),
    ),
    on(updateUserInformationFailure, (state, { error }) => ({
        ...state,
        error,
    })),
);
