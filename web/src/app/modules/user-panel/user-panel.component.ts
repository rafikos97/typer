import { ChangeDetectionStrategy, Component, Inject, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { fetchUserInformation } from './+state/user-panel.actions';

@Component({
    templateUrl: './user-panel.component.html',
    styleUrls: ['./user-panel.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-user-panel',
    standalone: true
})
export class UserPanelComponent implements OnInit {
    private readonly store = Inject(Store);

    ngOnInit(): void {
        this.store.dispatch(fetchUserInformation());
    }
}
