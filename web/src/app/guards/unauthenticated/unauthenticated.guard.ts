import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { map, tap } from 'rxjs/operators';
import { selectUserAuthenticated } from '../../modules/main/modules/authentication/+state/authentication.selectors';

export const unauthenticatedGuard: CanActivateFn = () => {
    const store = inject(Store);
    const router = inject(Router);
    return store.select(selectUserAuthenticated).pipe(
        tap((authenticated) => {
            if (authenticated) {
                router.navigateByUrl('/main/home');
            }
        }),
        map((authenticated) => !authenticated),
    );
};
