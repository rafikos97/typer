import { createReducer, on } from '@ngrx/store';
import { fetchUserInformationSuccess } from './user-panel.actions';

export interface UserPanelState {
    field?: any;
}

export const initialUserPanelState: UserPanelState = {};

export const userPanelReducer = createReducer(
    initialUserPanelState,
    on(fetchUserInformationSuccess, (state) => ({ ...state })),
);
