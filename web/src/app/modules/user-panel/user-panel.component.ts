import { ChangeDetectionStrategy, Component, importProvidersFrom } from '@angular/core';

@Component({
    templateUrl: './user-panel.component.html',
    styleUrls: ['./user-panel.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-user-panel',
    standalone: true
})
export class UserPanelComponent {
}