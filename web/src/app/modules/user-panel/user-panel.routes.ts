import { Route } from "@angular/router";
import { UserPanelComponent } from "./user-panel.component";

export const USER_PANEL_ROUTES: Route[] = [
    {
        path: '',
        component: UserPanelComponent,
        pathMatch: 'full'
    },
    {
        pathMatch: 'full',
        redirectTo: '',
        path: '**'
    }
]