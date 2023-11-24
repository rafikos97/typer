import { Routes } from '@angular/router';
import { DashboardComponent } from './modules/dashboard/dashboard.component';

export default [
    {
        path: 'home',
        component: DashboardComponent,
        title: 'Dashboard',
    },
    {
        path: 'user-panel',
        title: 'User Panel',
        loadChildren: () => import('./modules/user-panel/user-panel.routes'),
    },
    {
        path: 'admin',
        title: 'Admin',
        loadChildren: () => import('./modules/admin/admin.routes'),
    },
] as Routes;
