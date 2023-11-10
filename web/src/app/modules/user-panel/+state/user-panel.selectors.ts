import { createFeatureSelector } from '@ngrx/store';
import { userPanelFeatureKey } from './user-panel.feature-key';
import { UserPanelState } from './user-panel.reducer';

export const selectUserInformation =
    createFeatureSelector<UserPanelState>(userPanelFeatureKey);
