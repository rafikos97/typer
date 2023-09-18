import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { UserInformation } from '../../models/user-information.model';
import { userInformationApiUrl } from './user-panel.service.constants';
import { Observable } from 'rxjs';

@Injectable()
export class UserPanelService {
    private readonly httpClient = Inject(HttpClient);

    fetchUserInformation(): Observable<UserInformation> {
        return this.httpClient.get(userInformationApiUrl);
    }
}
