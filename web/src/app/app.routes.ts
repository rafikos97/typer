import { Routes } from '@angular/router';
import { DashboardComponent } from './modules/dashboard/dashboard.component';

export const APP_ROUTES: Routes = [
    {
        path: '',
        component: DashboardComponent,
        title: 'Dashboard',
    },
    {
        path: 'user-panel',
        title: 'User Panel',
        loadChildren: () =>
            import('./modules/user-panel/user-panel.routes').then(
                (routes) => routes.USER_PANEL_ROUTES,
            ),
    },
    {
        path: '**',
        pathMatch: 'full',
        redirectTo: '',
    },
];
