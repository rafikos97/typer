import { Route } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { UserPanelComponent } from './user-panel.component';
import { UserPanelEffects } from './+state/user-panel.effects';
import { provideState, provideStore } from '@ngrx/store';
import { userPanelReducer } from './+state/user-panel.reducer';

export const USER_PANEL_ROUTES: Route[] = [
    {
        path: '',
        component: UserPanelComponent,
        pathMatch: 'full',
        providers: [
            provideStore(),
            provideState({ name: 'userPanel', reducer: userPanelReducer }),
            provideEffects([UserPanelEffects]),
        ],
    },
    {
        pathMatch: 'full',
        redirectTo: '',
        path: '**',
    },
];
