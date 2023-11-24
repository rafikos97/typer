import { Routes } from '@angular/router';
import { LoginComponent } from './modules/login/login.component';
import { authenticatedGuard } from './guards/authenticated/authenticated.guard';
import { unauthenticatedGuard } from './guards/unauthenticated/unauthenticated.guard';
import { importProvidersFrom } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import {
    authenticationFeatureKey,
    authenticationReducer,
} from './modules/main/modules/authentication/+state/authentication.reducer';
import { AuthenticationService } from './modules/main/modules/authentication/services/authentication/authentication.service';
import { EffectsModule } from '@ngrx/effects';
import { AuthenticationEffects } from './modules/main/modules/authentication/+state/authentication.effects';

export const APP_ROUTES: Routes = [
    {
        path: '',
        title: 'Typer',
        providers: [
            importProvidersFrom(
                StoreModule.forFeature(
                    authenticationFeatureKey,
                    authenticationReducer,
                ),
            ),
            importProvidersFrom(
                EffectsModule.forFeature(AuthenticationEffects),
            ),
            AuthenticationService,
        ],
        children: [
            {
                path: 'login',
                component: LoginComponent,
                title: 'Login',
                canActivate: [unauthenticatedGuard],
                canDeactivate: [authenticatedGuard],
            },
            {
                path: 'main',
                loadChildren: () => import('./modules/main/main.routes'),
                canActivate: [authenticatedGuard],
            },
        ],
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: '/login',
    },
];
