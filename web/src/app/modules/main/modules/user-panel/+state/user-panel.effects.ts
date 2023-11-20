import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
    fetchUserInformation,
    fetchUserInformationSuccess,
    updateUserInformation,
    updateUserInformationFailure,
    updateUserInformationSuccess,
} from './user-panel.actions';
import { catchError, map, switchMap, withLatestFrom } from 'rxjs/operators';
import { UserPanelService } from '../services/user-panel/user-panel.service';
import { UserInformation } from '../models/user-information.model';
import { of } from 'rxjs';
import { Store } from '@ngrx/store';
import { selectUserInformation } from './user-panel.selectors';

@Injectable()
export class UserPanelEffects {
    private readonly userPanelService = inject(UserPanelService);
    private readonly actions$ = inject(Actions);
    private readonly store = inject(Store);

    readonly fetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(fetchUserInformation),
            switchMap(() => this.userPanelService.fetchUserInformation()),
            map((userInformation: UserInformation) =>
                fetchUserInformationSuccess({ userInformation }),
            ),
        ),
    );

    readonly update$ = createEffect(() =>
        this.actions$.pipe(
            ofType(updateUserInformation),
            withLatestFrom(this.store.select(selectUserInformation)),
            switchMap(([{ userInformation }, storeUserInfo]) =>
                this.userPanelService.updateUserInformation(
                    userInformation,
                    storeUserInfo.id,
                ),
            ),
            map((userInformation: UserInformation) =>
                updateUserInformationSuccess({ userInformation }),
            ),
            catchError((error) => of(updateUserInformationFailure({ error }))),
        ),
    );
}
