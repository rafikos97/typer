import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { UserInformation } from '../../models/user-information.model';
import { userInformationApiUrl } from './user-panel.service.constants';
import { Observable, of } from 'rxjs';

@Injectable()
export class UserPanelService {
    private readonly httpClient = inject(HttpClient);

    fetchUserInformation(): Observable<UserInformation> {
        return of(<UserInformation>{
            firstName: 'a',
            lastName: 'b',
            id: '1',
            username: 'my-user',
            email: 'a@b.com',
        });
        return this.httpClient.get<UserInformation>(userInformationApiUrl);
    }
}
