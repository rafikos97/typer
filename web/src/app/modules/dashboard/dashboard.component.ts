import { Component, ChangeDetectionStrategy } from '@angular/core';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: 'hello',
    styleUrls: [],
})
export class DashboardComponent {}
