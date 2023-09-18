import {
    ChangeDetectionStrategy,
    Component,
    OnInit,
    inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { fetchUserInformation } from './+state/user-panel.actions';
import { selectUserInformation } from './+state/user-panel.selectors';
import { UserPanelState } from './+state/user-panel.reducer';
import { Observable } from 'rxjs';
import { AsyncPipe, JsonPipe } from '@angular/common';

@Component({
    templateUrl: './user-panel.component.html',
    styleUrls: ['./user-panel.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-user-panel',
    standalone: true,
    imports: [AsyncPipe, JsonPipe],
})
export class UserPanelComponent implements OnInit {
    private readonly store = inject(Store);
    readonly userInformation$ = this.store.select(
        selectUserInformation,
    ) as Observable<UserPanelState>;

    ngOnInit(): void {
        this.store.dispatch(fetchUserInformation());
    }
}
