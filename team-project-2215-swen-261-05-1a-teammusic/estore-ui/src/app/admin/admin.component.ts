import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { User } from '../user';
import { UserService } from '../user.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  products: Product[] = [];
  product : Product;
  user: User;
  votes : number = 0;
  updatedUser : User = {} as User;
  constructor(private productService: ProductService, private router : Router,  private authService: AuthService, 
              private userService: UserService) { }


  ngOnInit(): void {
    this.authenticate();
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
    .subscribe(products => this.products = products);
  }

  authenticate(): void {
    this.user = this.authService.currentUser;
    if ((this.user.isAdmin) && (this.user.username != "null") && (this.user != null)){
      this.router.navigate(["/admin"])
    }
    else {
      this.router.navigate(["/login"])
    }
  }

  addProduct(name: string, price: number, quantity: number, image: string, video: string, upVotes:number,downVotes:number): void {
    name = name.trim();
    this.productService.addProduct( {name, price, quantity, image, video,upVotes,downVotes } as Product)
      .subscribe(product => {
        this.products.push(product);
    });
    
  }

  updateProduct(id:number, name: string, price: number, quantity: number, image: string, video: string,downVotes: number, upVotes:number): void {
    name = name.trim();
    this.productService.updateProduct( {id, name, price, quantity, image, video,upVotes,downVotes} as Product)
      .subscribe(product => {
        this.products.push(product);
        this.getProducts();
    });
  }

  delete(product: Product) {
    this.products = this.products.filter(p => p !== product);
    this.productService.deleteProduct(product.id).subscribe();
  }

}
