import { createReducer, on } from '@ngrx/store';
import { fetchUserInformationSuccess } from './user-panel.actions';

export interface UserPanelState {
    firstName: string;
    lastName: string;
    email: string;
    id: string;
    username: string;
}

export const initialUserPanelState: UserPanelState = {
    firstName: '',
    lastName: '',
    email: '',
    id: '',
    username: '',
};

export const userPanelReducer = createReducer(
    initialUserPanelState,
    on(fetchUserInformationSuccess, (state, { userInformation }) => ({
        ...state,
        ...userInformation,
    })),
);
