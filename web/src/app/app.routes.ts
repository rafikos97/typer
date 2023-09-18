import { ChangeDetectionStrategy, Component } from '@angular/core';
import { Routes } from '@angular/router';

@Component({
    selector: 'app-dashboard',
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: 'hello',
    styleUrls: [],
})
export class DashboardComponent {}

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
