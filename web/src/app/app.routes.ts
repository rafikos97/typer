import { Routes } from '@angular/router';

export const APP_ROUTES: Routes = [
    {
        path: 'user-panel',
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
