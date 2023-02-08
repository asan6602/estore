import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user';
import { UserService } from './user.service';

@Injectable({
   providedIn: 'root'
})
export class AuthService {
   constructor(private userService: UserService){ }
   isUserLoggedIn: boolean = false;
   nullUser : User = {username:"null"} as User;
   currentUser: User = this.nullUser;

   login(userName: string): Observable<User> {
      return this.userService.searchUsers(userName);
   }
   loginPassword(passWord: string): Observable<User> {
      return this.userService.searchPassword(passWord);
   }

   logout(): void {
      this.isUserLoggedIn = false;
      this.currentUser = this.nullUser;

   }
}