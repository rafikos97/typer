import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { LoginResponse } from '../../models/login-response.model';
import { loginApi } from './authentication.constants';

@Injectable()
export class AuthenticationService {
    private readonly httpClient = inject(HttpClient);

    login(username: string, password: string) {
        return this.httpClient.post<LoginResponse>(loginApi, {
            username,
            password,
        });
    }
}
