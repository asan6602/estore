import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Product } from '../product';
import { shoppingCart } from '../shoppingCart';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  login: string = "";
  cart: shoppingCart;
  user: User;
  isRemoveButtonVisible : boolean = false;
  iterableCart: Product[] = [];

  orderedProducts: Product[] = [];


  constructor(private userService: UserService, private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.getCart();
  }

  getCart(): void {
    this.user = this.authService.currentUser;

    if (this.user.isAdmin){
      this.router.navigate(['/']);  // Admin cannot have a cart so redirect to home page
      return;
    }

    if ((this.user.username != "null") && (this.user != null)){
      this.userService.getShoppingCart(this.user).subscribe(data => {
        if (data != undefined) {
          data.sort(function(a, b) {
            return - ( b.id - a.id);
          });
          
          let id = 0;
          let counter = 0;
          let quant = 0;

          for(let x = 0; x < data.length; x++){
            if(id == 0){
              id = data[x].id;
              quant = data[x].quantity;
            }

          
            if(id == data[x].id){
              counter++;
              if(counter <= quant){
                this.orderedProducts.push(data[x]);
              }
            } else {
              id = data[x].id
              quant = data[x].quantity;
              counter = 1;

              if(counter <= quant){
                this.orderedProducts.push(data[x]);
              }
            }
          } 

          this.iterableCart = this.orderedProducts;
          this.user.shoppingCart = this.orderedProducts;
          this.userService.updateUser(this.user).subscribe(replace => { this.iterableCart = replace.shoppingCart; });
          this.isRemoveButtonVisible = this.authService.currentUser != null ? true : false;
          this.login = "none";
        }
      });
    }
  }

  removeFromCart(p : Product): void {
    this.user = this.authService.currentUser;
    var index = this.user.shoppingCart.map(function(e) {return e.id}).indexOf(p.id);
    this.user.shoppingCart.splice(index,1);
    this.userService.updateUser(this.user).subscribe(data => {
      if (data != undefined) {
        this.iterableCart = data.shoppingCart;
      }
    });
  }

  navigateToProductPage(product: Product): void{
    this.router.navigate(['/products/details' + '/' + product.id]);
  }
  
  navigateToCheckout(): void{
    this.router.navigate(['/checkout']);
  }

  navigateToProducts(): void{
    this.router.navigate(['/products']);
  }

}
