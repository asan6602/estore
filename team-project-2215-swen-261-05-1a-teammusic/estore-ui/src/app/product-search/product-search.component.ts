import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';
import { AuthService } from '../auth.service';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: [ './product-search.component.css' ]
})
export class ProductSearchComponent implements OnInit {
  products$!: Observable<Product[]>;
  private searchTerms = new Subject<string>();

  products: Product[] = [];
  product : Product;
  user: User;
  updatedUser : User = {} as User;

  constructor(private productService: ProductService, private authService: AuthService, private router : Router,
              private userService: UserService) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.products$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.productService.searchProducts(term)),
    );
  }

  addToCart(product: Product): void {
    this.user = this.authService.currentUser;
    this.product = product;

    if (this.user.username == 'null' || this.user == null) {
      this.router.navigate(['/login']);
    }

    this.user.shoppingCart.push(this.product);
    
    this.userService.updateUser(this.user).subscribe(data => {this.updatedUser = data});
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
}
