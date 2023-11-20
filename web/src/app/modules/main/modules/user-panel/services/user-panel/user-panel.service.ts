import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { UserInformation } from '../../models/user-information.model';
import {
    userInformationGetApiUrl,
    userInformationPutApiUrl,
} from './user-panel.service.constants';
import { Observable } from 'rxjs';

@Injectable()
export class UserPanelService {
    private readonly httpClient = inject(HttpClient);

    fetchUserInformation(): Observable<UserInformation> {
        return this.httpClient.get<UserInformation>(userInformationGetApiUrl);
    }

    updateUserInformation(
        userInformation: UserInformation,
        userId: string,
    ): Observable<UserInformation> {
        return this.httpClient.put<UserInformation>(
            `${userInformationPutApiUrl}/${userId}`,
            userInformation,
        );
    }
}
