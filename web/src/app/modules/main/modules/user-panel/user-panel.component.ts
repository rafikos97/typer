import {
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    OnInit,
    inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
    fetchUserInformation,
    updateUserInformation,
} from './+state/user-panel.actions';
import { selectUserInformation } from './+state/user-panel.selectors';
import { AsyncPipe, NgIf } from '@angular/common';
import { UserInformation } from './models/user-information.model';
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators,
} from '@angular/forms';

@Component({
    templateUrl: './user-panel.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-user-panel',
    standalone: true,
    imports: [AsyncPipe, ReactiveFormsModule, NgIf],
})
export class UserPanelComponent implements OnInit {
    private readonly store = inject(Store);
    private readonly changeDetectorRef = inject(ChangeDetectorRef);

    readonly userForm = new FormGroup({
        username: new FormControl('', Validators.required),
        firstName: new FormControl('', Validators.required),
        lastName: new FormControl('', Validators.required),
        email: new FormControl('', [Validators.required, Validators.email]),
    });

    readonly storeToFormSubscription = this.store
        .select(selectUserInformation)
        .pipe(takeUntilDestroyed())
        .subscribe(
            ({ username, firstName, lastName, email }: UserInformation) => {
                this.userForm.setValue({
                    username,
                    firstName,
                    lastName,
                    email,
                });

                this.changeDetectorRef.markForCheck();
            },
        );

    ngOnInit(): void {
        this.store.dispatch(fetchUserInformation());
    }

    get dataIsFetched(): boolean {
        return !!this.userForm.value!.username;
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
