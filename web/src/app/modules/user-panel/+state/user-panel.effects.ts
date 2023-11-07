import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
    fetchUserInformation,
    fetchUserInformationSuccess,
    updateUserInformation,
    updateUserInformationFailure,
    updateUserInformationSuccess,
} from './user-panel.actions';
import { catchError, map, switchMap } from 'rxjs/operators';
import { UserPanelService } from '../services/user-panel/user-panel.service';
import { UserInformation } from '../models/user-information.model';
import { of } from 'rxjs';

@Injectable()
export class UserPanelEffects {
    private readonly userPanelService = inject(UserPanelService);
    private readonly actions$ = inject(Actions);

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
            switchMap(({ userInformation }) =>
                this.userPanelService.updateUserInformation(userInformation),
            ),
            map((userInformation: UserInformation) =>
                updateUserInformationSuccess({ userInformation }),
            ),
            catchError((error) => of(updateUserInformationFailure({ error }))),
        ),
    );
}
