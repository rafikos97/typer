import { Inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import {
    fetchUserInformation,
    fetchUserInformationSuccess,
} from './user-panel.actions';
import { map, switchMap } from 'rxjs/operators';
import { UserPanelService } from '../services/user-panel/user-panel.service';
import { UserInformation } from '../models/user-information.model';

@Injectable()
export class UserPanelEffects {
    private readonly userPanelService = Inject(UserPanelService);
    private readonly actions$ = Inject(Actions);

    readonly fetch$ = createEffect(() =>
        this.actions$.pipe(
            ofType(fetchUserInformation),
            switchMap(() => this.userPanelService.fetchUserInformation()),
            map((userInformation: UserInformation) =>
                fetchUserInformationSuccess({ userInformation }),
            ),
        ),
    );
}
