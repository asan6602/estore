import { Component } from '@angular/core';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private authService: AuthService){}
  title = 'Music Store ROC';

  public get authenticated(): boolean {
    return this.authService.isUserLoggedIn;
  }

  public get isAdmin(): boolean {
    return this.authService.currentUser.isAdmin;
  }
}
