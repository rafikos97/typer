import {
  provideRouter,
  withPreloading,
  PreloadAllModules,
  withDebugTracing,
} from '@angular/router';
import { AppComponent } from './app/app.component';
import { bootstrapApplication } from '@angular/platform-browser';
import { APP_ROUTES } from './app/app.routes';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(
      APP_ROUTES,
      withPreloading(PreloadAllModules),
      withDebugTracing(),
    ),
  ],
}).catch((err) => console.error(err));
