import { Component, inject } from '@angular/core';
import { LoginService } from './services/login/login.service';
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { loginSuccess } from '../main/modules/authentication/+state/authentication.actions';
import { AnyFn } from '@ngrx/store/src/selector';

@Component({
    standalone: true,
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
    selector: 'app-login',
    providers: [LoginService],
    imports: [ReactiveFormsModule],
})
export class LoginComponent {
    private readonly loginService = inject(LoginService);
    private readonly router = inject(Router);
    private readonly store = inject(Store);

    readonly loginForm = new FormGroup({
        username: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required),
    });

    login() {
        const { username, password } = this.loginForm.value;
        this.loginService
            .login(username || '', password || '')
            .subscribe((response: any) => {
                this.router.navigateByUrl('/');
                const token = response['access_token'];
                const expires = response['expires_in'];
                this.store.dispatch(loginSuccess({ token, expires }));
            });
    }
}
