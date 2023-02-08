import { Component, ElementRef, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Product } from '../product';
import { User } from '../user';
import { UserService } from '../user.service';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  user: User
  iterableCart: Product[] = [];
  orderedProducts: Product[] = [];
  initialProducts: Product[] = [];
  product : Product;

  failText = "";
  isFail: boolean = false;
  isEmpty: boolean = false;
  isSubmitShown: boolean = true;
  isLogin: boolean = false;

  price: number = 0.00;

  constructor(private userService: UserService, private router: Router, private authService: AuthService, private productService: ProductService) { }

  ngOnInit(): void {
    this.getCart();
  }

  getCart(): void {

    this.user = this.authService.currentUser;

    if(this.user.isAdmin){ this.router.navigate(['/']); return; }

    if ((this.user.username != "null") && this.user != null){
      this.isLogin = false;
      this.userService.getShoppingCart(this.user).subscribe(data =>
        {
          if(data != undefined) {
            this.isEmpty = false;
            this.isSubmitShown = true;

            data.sort(function(a, b) {
              return - ( b.id - a.id);
            });

            
            for(let i = 0; i < data.length; i++){
                this.productService.getProduct(data[i].id).subscribe(pulledData =>{
                  if(pulledData.quantity != data[i].quantity){
                    data[i].quantity = pulledData.quantity;
                  }
                });
            }

            this.orderedProducts = data;
            this.iterableCart = this.orderedProducts;

            for(let i = 0; i < this.orderedProducts.length; i++) {
                this.addToPrice(this.orderedProducts[i]);
            }
          }

          if(data.length == 0){
            this.isEmpty = true;
            this.isSubmitShown = false;
          }
        });

    }
    else{
      this.isSubmitShown = false;
      this.isLogin = true;
    }
  }

  addToPrice(product: Product){
    this.product = product;
    this.price += product.price;
  }

  updateProducts():void{
      for(let i = 0; i < this.orderedProducts.length; i++){
        this.productService.getProduct(this.orderedProducts[i].id).subscribe(data => {
          this.orderedProducts[i] = data;
        })
      }

      this.iterableCart = this.orderedProducts;
  }

  clearCart(): void{
    this.user = this.authService.currentUser;
    if ((this.user.username != "null") && (this.user != null)){

      this.decreaseQuantity();

        this.user.shoppingCart.forEach((element,index)=> {  this.user.shoppingCart.splice(index, this.user.shoppingCart.length) });
        this.userService.updateUser(this.user).subscribe(data => { this.iterableCart = data.shoppingCart; });
    }
   
      this.price = 0.00;
      this.isEmpty = true;
      this.isSubmitShown = false;
    
  }

  decreaseQuantity(): void{

    this.updateProducts();

    let id = this.orderedProducts[0].id;
    let tally = 0;

    for(let i = 0; i < this.orderedProducts.length; i++){
    
      if(id == this.orderedProducts[i].id){
        tally++;

        if( (i + 1) == this.orderedProducts.length && this.isFail == false){
          this.orderedProducts[i].quantity -= tally;

          if(this.orderedProducts[i].quantity < 0){
            let diff = tally - (-1)*(this.orderedProducts[i].quantity);
            this.failText = "Error: There are only " + diff + " item(s) of item ID " + this.orderedProducts[i].id + ": '" + this.orderedProducts[i].name + "' available in stock. You attempted to purchase " + tally + " of them.";
            this.isFail = true;
            return;

          } else {
            this.update(this.orderedProducts[i]);
          }

        }

      } else {

        this.orderedProducts[i-1].quantity -= tally;
        if(this.orderedProducts[i-1].quantity < 0){
          let diff = tally - (-1)*(this.orderedProducts[i-1].quantity);
          this.failText = "Error: There are only " + diff + " item(s) of item ID " + this.orderedProducts[i-1].id + ": '" + this.orderedProducts[i-1].name + "' available in stock. You attempted to purchase " + tally + " of them.";
          this.isFail = true;
          return
        } else{
          this.update(this.orderedProducts[i-1]);
        }

        id = this.orderedProducts[i].id;
        tally = 1;

        this.productService.getProduct(id).subscribe(data => {
          if(tally > data.quantity){
           this.orderedProducts[i].quantity = data.quantity;
          } 
        });

        if( (i + 1) == this.orderedProducts.length && this.isFail == false){
          this.orderedProducts[i].quantity -= tally;
          if(this.orderedProducts[i].quantity < 0){
            let diff = tally - (-1)*(this.orderedProducts[i].quantity);
            this.failText = "Error: There are only " + diff + " item(s) of item ID " + this.orderedProducts[i].id + ": '" + this.orderedProducts[i].name + "' available in stock. You attempted to purchase " + tally + " of them.";
            this.isFail = true;
            return;
          } else {
            this.update(this.orderedProducts[i]);
          }
        }
      }
    }
  }


  update(product: Product){
    this.productService.updateProduct(product).subscribe();

  }

  navigateToLogin(): void{
    this.router.navigate(['/login']);
  }

  navigateToProducts(): void{
    this.router.navigate(['/products']);
  }

  navigateToCart(): void{
    this.router.navigate(['/cart']);
  }
  
}
