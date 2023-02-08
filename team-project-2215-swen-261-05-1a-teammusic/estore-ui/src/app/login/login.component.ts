import { Component, OnInit } from '@angular/core';

import { FormGroup, FormControl } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { User } from '../user';
import { UserService } from '../user.service';
import { Product } from '../product';

@Component({
   selector: 'app-login',
   templateUrl: './login.component.html',
   styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
   login: string = "";
   popup: string = "none";
   userName: string;
   formData: FormGroup;
   Users: User[] = [];


   constructor(private authService : AuthService, private router : Router, private userService: UserService) { }

   ngOnInit(): void {
      this.formData = new FormGroup({
         userName: new FormControl("admin")
      });
      this.authService.logout()
   }

   onClickSubmit(uname: string, pword: string): void {
      this.userName = uname;
      
      this.authService.login(uname).subscribe(
         data => {if (data != null) {
               this.authService.loginPassword(pword).subscribe(
                  newData => {if (newData != null){
                     this.authService.isUserLoggedIn = true;
                     this.authService.currentUser = newData;
                     if (this.authService.currentUser.isAdmin) {
                        this.router.navigate(['/admin/']);
                     }
                     else {
                        this.router.navigate(['/products/']);
                     }
                  }});

         }});
   }

   createUser(username: string, password : string, isAdmin: boolean, shoppingCart: Array<Product>, wishlist: Array<Product>): void {
      username = username.trim();
      password = password.trim();
      this.userService.createUser( {username, password, isAdmin, shoppingCart, wishlist} as User)
      .subscribe(User => {
         this.Users.push(User);
         if (User != null) {
            this.displayPopup("open");
         }
     });

   }

   displayPopup(open: String): void {
      if (open == "open") {
        this.popup = "";
      } 
      else {
        this.popup = "none";
      }
    }

}
