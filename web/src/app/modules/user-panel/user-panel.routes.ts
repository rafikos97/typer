import { Route } from '@angular/router';
import { EffectsModule } from '@ngrx/effects';
import { UserPanelComponent } from './user-panel.component';
import { UserPanelEffects } from './+state/user-panel.effects';
import { userPanelReducer } from './+state/user-panel.reducer';
import { importProvidersFrom } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { userPanelFeatureKey } from './+state/user-panel.feature-key';
import { UserPanelService } from './services/user-panel/user-panel.service';

export const USER_PANEL_ROUTES: Route[] = [
    {
        path: '',
        children: [{ path: '', component: UserPanelComponent }],
        providers: [
            UserPanelService,
            importProvidersFrom(
                StoreModule.forFeature(userPanelFeatureKey, userPanelReducer),
            ),
            importProvidersFrom(EffectsModule.forFeature([UserPanelEffects])),
        ],
    },
];
