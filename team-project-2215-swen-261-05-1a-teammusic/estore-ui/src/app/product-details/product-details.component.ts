import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { User } from '../user';
import { UserService } from '../user.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit {
  product: Product = {} as Product;
  user: User;
  updatedUser : User = {} as User;
  popup: string = "none";

  test: boolean = true;

  constructor(
    private productService: ProductService, private router : Router, private route: ActivatedRoute, 
      private authService: AuthService, private userService: UserService, private location: Location
  ) { }

  ngOnInit(): void {
    this.getProduct();
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

  upVoteProduct(product:Product):void
  {
    this.product.upVotes = this.product.upVotes+1;
    this.productService.updateProduct(this.product).subscribe();
    this.test = false;
  }

  downVoteProduct(product: Product):void
  {
    this.product.downVotes = this.product.downVotes + 1;
    this.productService.updateProduct(this.product).subscribe();
    this.test = false;
  }
  getProduct(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProduct(id)
      .subscribe(product => {
        this.product = product; 
        let u = <HTMLIFrameElement>document.getElementById("videoPlayer");
        u.src = this.product.video;
        this.updateProducts();
      });
  }

  goBack(): void {
    this.location.back();
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
      this.productService.getProduct(this.product.id).subscribe(data => {
        this.product = data;
      })
  }
}
