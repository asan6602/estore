import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
  popup: string = "none";
  products: Product[] = [];
  product : Product;
  user: User;
  updatedUser : User = {} as User;
  constructor(private productService: ProductService, private router : Router,  private authService: AuthService, 
              private userService: UserService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
    .subscribe(products => this.products = products);
  }

  addToCart(product: Product): void {
    this.user = this.authService.currentUser;
    this.product = product;

    if (this.user.username == 'null' || this.user == null) {
      this.router.navigate(['/login']);
    }

    if(product.quantity == 0) {
      this.displayPopup("open");
    }
    else {
      this.user.shoppingCart.push(this.product);
      this.userService.updateUser(this.user).subscribe(data => {this.updatedUser = data});
    }
  }

  addToWishlist(product: Product): void {
    this.user = this.authService.currentUser;
    this.product = product;
    let found = true;
    let i = 0;

    if (this.user.username == 'null' || this.user == null) {
      this.router.navigate(['/login']);
    }

    while(found && this.user.wishlist[i] != null) {
      if(this.user.wishlist[i].id == product.id) {
        found = false;
      }
      i++;
    }

    if(found) {
      this.user.wishlist.push(this.product);
    
      this.userService.updateUser(this.user).subscribe(data => {this.updatedUser = data});
    }
  }

  navigateToProductPage(product: Product): void{
    this.router.navigate(['/products/details' + '/' + product.id]);
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