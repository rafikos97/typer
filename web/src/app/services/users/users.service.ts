import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { baseApiPath } from 'src/app/base-api-path';
import { User, Users } from '../../models/users.model';

@Injectable()
export class UsersService {
    private readonly matchesApi = `${baseApiPath}/profile`;
    private readonly httpClient = inject(HttpClient);

    getUsers() {
        return this.httpClient.get<Users>(`${this.matchesApi}/all`);
    }

    createUser(match: Omit<User, 'id'>) {
        return this.httpClient.post<User>(`${this.matchesApi}/register`, match);
    }

    getUser(id: string) {
        return this.httpClient.get<User>(`${this.matchesApi}/${id}`);
    }

    updateUser(match: User, id: string) {
        return this.httpClient.post<User>(`${this.matchesApi}/${id}`, match);
    }

    deleteUser(id: string) {
        return this.httpClient.delete<User>(`${this.matchesApi}/${id}`);
    }
}
