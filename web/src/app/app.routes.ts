import { Routes } from '@angular/router';
import { LoginComponent } from './modules/login/login.component';
import { authenticatedGuard } from './guards/authenticated/authenticated.guard';
import { unauthenticatedGuard } from './guards/unauthenticated/unauthenticated.guard';
import { importProvidersFrom } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { MatchesEffects } from './+state/match/match.effects';
import { matchesFeatureKey } from './+state/match/match.feature-key';
import { matchesReducer } from './+state/match/match.reducer';
import { PointRulesEffects } from './+state/point-rules/point-rules.effects';
import { pointRulesFeatureKey } from './+state/point-rules/point-rules.feature-key';
import { pointRulesReducer } from './+state/point-rules/point-rules.reducer';
import { TournamentsEffects } from './+state/tournament/tournament.effects';
import { tournamentsFeatureKey } from './+state/tournament/tournament.feature-key';
import { tournamentsReducer } from './+state/tournament/tournament.reducer';
import { MatchesService } from './services/matches/matches.service';
import { PointRulesService } from './services/point-rules/point-rules.service';
import { TournamentsService } from './services/tournaments/tournaments.service';
import { usersFeatureKey } from './+state/users/users.feature-key';
import { usersReducer } from './+state/users/users.reducer';
import { UsersEffects } from './+state/users/users.effects';
import { UsersService } from './services/users/users.service';
import { applicationContextFeatureKey } from './+state/application-context/application-context.feature-key';
import { applicationContextReducer } from './+state/application-context/application-context.reducer';

export const APP_ROUTES: Routes = [
    {
        path: '',
        title: 'Typer',
        providers: [
            importProvidersFrom(
                StoreModule.forFeature(
                    tournamentsFeatureKey,
                    tournamentsReducer,
                ),
                StoreModule.forFeature(usersFeatureKey, usersReducer),
                EffectsModule.forFeature([
                    TournamentsEffects,
                    PointRulesEffects,
                    MatchesEffects,
                    UsersEffects,
                ]),
                StoreModule.forFeature(pointRulesFeatureKey, pointRulesReducer),
                StoreModule.forFeature(matchesFeatureKey, matchesReducer),
                StoreModule.forFeature(
                    applicationContextFeatureKey,
                    applicationContextReducer,
                ),
            ),
            PointRulesService,
            TournamentsService,
            MatchesService,
            UsersService,
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
