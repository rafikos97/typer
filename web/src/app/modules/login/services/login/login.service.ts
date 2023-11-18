import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { loginApi } from '../../login.constants';
import { LoginResponse } from '../../models/login-response.model';

@Injectable()
export class LoginService {
    private readonly httpClient = inject(HttpClient);

    login(username: string, password: string) {
        return this.httpClient.post<LoginResponse>(loginApi, {
            username,
            password,
        });
    }
}
