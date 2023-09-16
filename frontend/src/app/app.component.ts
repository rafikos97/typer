import { Component } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  standalone: true,
  imports: [AppRoutingModule]
})
export class AppComponent {
  title = 'Typer';
}
