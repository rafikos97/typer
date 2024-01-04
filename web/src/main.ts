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
import {
    HTTP_INTERCEPTORS,
    provideHttpClient,
    withInterceptorsFromDi,
} from '@angular/common/http';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { AuthenticationInterceptor } from './app/modules/main/modules/authentication/interceptors/authentication.interceptor';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { AuthenticationEffects } from './app/modules/main/modules/authentication/+state/authentication.effects';
import {
    authenticationFeatureKey,
    authenticationReducer,
} from './app/modules/main/modules/authentication/+state/authentication.reducer';

bootstrapApplication(AppComponent, {
    providers: [
        provideHttpClient(withInterceptorsFromDi()),
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthenticationInterceptor,
            multi: true,
        },
        provideRouter(APP_ROUTES, withPreloading(PreloadAllModules)),
        importProvidersFrom(
            StoreModule.forRoot({
                [authenticationFeatureKey]: authenticationReducer,
            }),
        ),
        importProvidersFrom(EffectsModule.forRoot([AuthenticationEffects])),
        importProvidersFrom(StoreDevtoolsModule.instrument()),
    ],
}).catch((err) => console.error(err));
