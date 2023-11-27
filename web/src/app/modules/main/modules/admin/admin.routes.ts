import { Routes } from '@angular/router';
import { AdminPanelComponent } from './admin-panel.component';
import { TournamentsService } from './services/tournaments/tournaments.service';
import { tournamentsFeatureKey as tournamentsFeatureKey } from './+state/tournament/tournament.feature-key';
import { tournamentsReducer } from './+state/tournament/tournament.reducer';
import { importProvidersFrom } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { TournamentsEffects } from './+state/tournament/tournament.effects';
import { PointRulesEffects } from './+state/point-rules/point-rules.effects';
import { pointRulesFeatureKey } from './+state/point-rules/point-rules.feature-key';
import { pointRulesReducer } from './+state/point-rules/point-rules.reducer';
import { PointRulesService } from './services/point-rules/point-rules.service';

export default [
    {
        path: '',
        providers: [
            TournamentsService,
            PointRulesService,
            importProvidersFrom(
                StoreModule.forFeature(
                    tournamentsFeatureKey,
                    tournamentsReducer,
                ),
                StoreModule.forFeature(pointRulesFeatureKey, pointRulesReducer),
            ),
            importProvidersFrom(
                EffectsModule.forFeature([
                    TournamentsEffects,
                    PointRulesEffects,
                ]),
            ),
        ],
        children: [
            {
                component: AdminPanelComponent,
                path: '',
            },
        ],
    },
] as Routes;
