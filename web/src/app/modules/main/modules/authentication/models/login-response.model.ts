import { Scope } from './scope.model';

export interface LoginResponse {
    accessToken: string;
    tokenType: string;
    expiresIn: number;
    scope: Scope;
    refreshToken: string;
}
