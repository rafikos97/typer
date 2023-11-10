import {
    provideRouter,
    withPreloading,
    PreloadAllModules,
    withDebugTracing,
} from '@angular/router';
import { AppComponent } from './app/app.component';
import { bootstrapApplication } from '@angular/platform-browser';
import { APP_ROUTES } from './app/app.routes';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';

bootstrapApplication(AppComponent, {
    providers: [
        importProvidersFrom(HttpClientModule),
        provideRouter(
            APP_ROUTES,
            withPreloading(PreloadAllModules),
            withDebugTracing(),
        ),
        importProvidersFrom(StoreModule.forRoot({})),
        importProvidersFrom(EffectsModule.forRoot()),
    ],
}).catch((err) => console.error(err));
