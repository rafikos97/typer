import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { Store } from '@ngrx/store';
import { selectUserAuthenticated } from 'src/app/modules/main/modules/authentication/+state/authentication.selectors';

export const authenticatedGuard: CanActivateFn = () => {
    const store = inject(Store);
    return store.select(selectUserAuthenticated);
};
