import { Routes } from '@angular/router';
import { AdminPanelComponent } from './admin-panel.component';

export default [
    {
        path: '',
        children: [
            {
                component: AdminPanelComponent,
                path: '',
            },
        ],
    },
] as Routes;
