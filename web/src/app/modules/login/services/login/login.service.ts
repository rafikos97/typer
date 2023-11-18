import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { loginApi } from '../../login.constants';

@Injectable()
export class LoginService {
    private readonly httpClient = inject(HttpClient);

    login(username: string, password: string) {
        return this.httpClient.post(loginApi, { username, password });
    }
}
