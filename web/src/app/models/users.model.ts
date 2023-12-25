export interface User {
    firstName: string;
    lastName: string;
    username: string;
    email: string;
    id?: number;
    password: string;
}

export type Users = User[];
