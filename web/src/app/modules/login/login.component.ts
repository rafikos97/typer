import { Component, inject } from '@angular/core';
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { Store } from '@ngrx/store';
import { login } from '../main/modules/authentication/+state/authentication.actions';

@Component({
    standalone: true,
    templateUrl: './login.component.html',
    selector: 'app-login',
    imports: [ReactiveFormsModule],
})
export class LoginComponent {
    private readonly store = inject(Store);

    readonly loginForm = new FormGroup({
        username: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required),
    });

    login() {
        const { username, password } = this.loginForm.value;
        this.store.dispatch(
            login({ username: username || '', password: password || '' }),
        );
    }
}
