import { createReducer, on } from '@ngrx/store';
import {
    fetchUserInformationSuccess,
    updateUserInformationSuccess,
} from './user-panel.actions';

export interface UserPanelState {
    firstName: string;
    lastName: string;
    email: string;
    id: number;
    username: string;
}

export const initialUserPanelState: UserPanelState = {
    firstName: '',
    lastName: '',
    email: '',
    id: 0,
    username: '',
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
);
