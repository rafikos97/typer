import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest,
} from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { first, switchMap } from 'rxjs/operators';
import { selectAuthentication } from '../+state/authentication.selectors';
import { AuthenticationState } from '../+state/authentication.reducer';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {
    private readonly store = inject(Store);

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler,
    ): Observable<HttpEvent<any>> {
        return this.store.select(selectAuthentication).pipe(
            first(),
            switchMap((auth: AuthenticationState) => {
                return next.handle(
                    req.clone({
                        setHeaders: {
                            Authorization: auth.token,
                        },
                    }),
                );
            }),
        );
    }
}
