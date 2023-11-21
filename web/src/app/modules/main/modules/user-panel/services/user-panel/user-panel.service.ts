import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { UserInformation } from '../../models/user-information.model';
import { userInformationApiUrl } from './user-panel.service.constants';
import { Observable } from 'rxjs';

@Injectable()
export class UserPanelService {
    private readonly httpClient = inject(HttpClient);

    fetchUserInformation(): Observable<UserInformation> {
        return this.httpClient.get<UserInformation>(userInformationApiUrl);
    }

    updateUserInformation(
        userInformation: UserInformation,
        userId: number,
    ): Observable<UserInformation> {
        return this.httpClient.put<UserInformation>(
            `${userInformationApiUrl}${userId}`,
            userInformation,
        );
    }
}
