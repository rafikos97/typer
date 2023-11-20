import { Routes } from '@angular/router';
import { DashboardComponent } from './modules/dashboard/dashboard.component';

export default [
    {
        path: '',
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
                    import('./modules/user-panel/user-panel.routes'),
            },
        ],
    },
] as Routes;
