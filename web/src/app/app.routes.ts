// app.routes.ts

import { Routes } from '@angular/router';
import { AppComponent } from './app.component';

export const APP_ROUTES: Routes = [
    {
        path: 'home',
        component: AppComponent,
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: 'home',
    },
];
