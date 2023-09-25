import {
    ChangeDetectionStrategy,
    Component,
    OnInit,
    Signal,
    computed,
    inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import {
    fetchUserInformation,
    updateUserInformation,
} from './+state/user-panel.actions';
import { selectUserInformation } from './+state/user-panel.selectors';
import { AsyncPipe, JsonPipe } from '@angular/common';
import { UserInformation } from './models/user-information.model';
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';
import { first } from 'rxjs/operators';

@Component({
    templateUrl: './user-panel.component.html',
    styleUrls: ['./user-panel.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-user-panel',
    standalone: true,
    imports: [AsyncPipe, JsonPipe, ReactiveFormsModule],
})
export class UserPanelComponent implements OnInit {
    private readonly store = inject(Store);

    readonly userForm = new FormGroup({
        username: new FormControl('', Validators.required),
        firstName: new FormControl('', Validators.required),
        lastName: new FormControl('', Validators.required),
        email: new FormControl('', [Validators.required, Validators.email]),
    });

    ngOnInit(): void {
        this.store.dispatch(fetchUserInformation());
        this.store
            .select(selectUserInformation)
            .pipe(first())
            .subscribe(
                ({ username, firstName, lastName, email }: UserInformation) => {
                    this.userForm.setValue({
                        username,
                        firstName,
                        lastName,
                        email,
                    });
                },
            );
    }

    updateUserInformation(): void {
        this.userForm.markAllAsTouched();
        this.userForm.updateValueAndValidity();
        if (this.userForm.invalid) {
            return;
        }

        const userInformation: UserInformation = this.userForm
            .value as UserInformation;
        this.store.dispatch(updateUserInformation({ userInformation }));
    }
}
