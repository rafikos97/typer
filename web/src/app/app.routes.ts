import { Routes } from '@angular/router';
import { DashboardComponent } from './modules/main/modules/dashboard/dashboard.component';
import { LoginComponent } from './modules/login/login.component';
import {
    authenticationFeatureKey,
    authenticationReducer,
} from './modules/main/modules/authentication/+state/authentication.reducer';
import { StoreModule } from '@ngrx/store';
import { importProvidersFrom } from '@angular/core';

export const APP_ROUTES: Routes = [
    {
        path: '',
        title: 'Typer',
        children: [
            {
                path: 'login',
                component: LoginComponent,
                title: 'Login',
            },
            {
                path: 'main',
                providers: [
                    importProvidersFrom(
                        StoreModule.forFeature(
                            authenticationFeatureKey,
                            authenticationReducer,
                        ),
                    ),
                ],
                children: [
                    {
                        path: '',
                        component: DashboardComponent,
                        title: 'Dashboard',
                    },
                    {
                        path: 'user-panel',
                        title: 'User Panel',
                        loadChildren: () =>
                            import(
                                './modules/main/modules/user-panel/user-panel.routes'
                            ).then((routes) => routes.USER_PANEL_ROUTES),
                    },
                ],
            },
        ],
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: '',
    },
];
