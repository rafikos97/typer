import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { LoginResponse } from '../../models/login-response.model';
import { loginApi, refreshTokenApi } from './authentication.constants';
import { Observable } from 'rxjs';

@Injectable()
export class AuthenticationService {
    private readonly httpClient = inject(HttpClient);

    login(username: string, password: string): Observable<LoginResponse> {
        return this.httpClient.post<LoginResponse>(loginApi, {
            username,
            password,
        });
    }

    refreshToken(refreshToken: string): Observable<LoginResponse> {
        return this.httpClient.post<LoginResponse>(refreshTokenApi, {
            refreshToken,
        });
    }
}
