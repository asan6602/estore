import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Product } from '../product';
import { wishlist } from '../wishlist';
import { User } from '../user';
import { UserService } from '../user.service';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  login: string = "";
  logged: string = "none";
  popup: string = "none";
  wishlist: wishlist;
  user: User;
  isRemoveButtonVisible : boolean = false;
  updatedUser : User = {} as User;
  tempWishlist: Product[] = [];
  products: Product[] = [];
  constructor(private userService: UserService, private route: Router, private authService: AuthService, private productService: ProductService) { }

  ngOnInit(): void {
    this.getWishList();
  }

  getWishList(): void {
    this.user = this.authService.currentUser;

    if (this.user.isAdmin){
      this.route.navigate(['/']);  // Admin cannot have a cart so redirect to home page
      return;
    }

    if ((this.user.username != "null") && (this.user != null)){
      this.userService.getWishList(this.user).subscribe(data => {
        if (data != undefined) {
          this.tempWishlist = this.user.wishlist;
          this.updateProducts();
          this.isRemoveButtonVisible = this.authService.currentUser != null ? true : false;
          this.login = "none";
          this.logged = "";
        }
      });
    }
  }

  removeFromList(product:Product): void {
    this.user = this.authService.currentUser;

    if ((this.user.username != "null") && (this.user != null)){ // This loops through the list, looking to remove product whne found
      this.user.wishlist.forEach((element,index)=> {
        if(element.id == product.id) {
          this.user.wishlist.splice(index,1);
        }
      });

      this.userService.updateUser(this.user).subscribe(data => {this.updatedUser = data});
      this.getWishList();
    }
  }

  ListToCart(product:Product): void {
    let found = true;
    this.user = this.authService.currentUser;

    if(product.quantity == 0) {
      this.displayPopup("open");
      found = false;
    }

    for(let i = 0; found && i < this.user.shoppingCart.length; i++) {
      if(this.user.shoppingCart[i].id == product.id) {
        found = false;
      }
    }

    if(found) {
      this.user.shoppingCart.push(product);
      this.removeFromList(product);
    
      this.userService.updateUser(this.user).subscribe(data => {this.updatedUser = data});
    }
  }

  navigateToProductPage(product: Product): void{
    this.route.navigate(['/products/details' + '/' + product.id]);
  }

  displayPopup(open: String): void {
    if (open == "open") {
      this.popup = "";
    } 
    else {
      this.popup = "none";
    }
  }

  updateProducts():void{
    for(let i = 0; i < this.tempWishlist.length; i++){
      this.productService.getProduct(this.tempWishlist[i].id).subscribe(data => {
        this.tempWishlist[i] = data;
      })
    }
  }
}
