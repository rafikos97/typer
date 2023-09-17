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
import { provideStore } from '@ngrx/store';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import { provideRouterStore } from '@ngrx/router-store';

bootstrapApplication(AppComponent, {
    providers: [
        provideRouter(
            APP_ROUTES,
            withPreloading(PreloadAllModules),
            withDebugTracing(),
        ),
        provideStore(),
        provideStoreDevtools(),
        provideRouterStore(),
        importProvidersFrom(HttpClientModule),
    ],
}).catch((err) => console.error(err));
