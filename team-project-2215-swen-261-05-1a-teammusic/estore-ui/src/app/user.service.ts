import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders} from '@angular/common/http'
import { catchError } from 'rxjs/operators';
import { User } from './user';
import { Product } from './product';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }
  private usersUrl = 'http://localhost:8080/UserAccounts';
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  /**
 * Handle Http operation that failed.
 * Let the app continue.
 *
 * @param operation - name of the operation that failed
 * @param result - optional value to return as the observable result
 */
private handleError<T>(operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {

    console.error(error); // log to console instead

    return of(result as T);
  };
}

    /** POST: add a new user to the server */
  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.usersUrl, user, this.httpOptions).pipe(
      catchError(this.handleError<User>('addUser'))
    );
  }

    /* GET heroes whose name contains search term */
  searchUsers(term: string): Observable<User> {
    return this.http.get<User>(`${this.usersUrl}/?username=${term}`).pipe(
      catchError(this.handleError<User>('searchUsers'))
    );
  }
  searchPassword(term: string): Observable<User> {
    return this.http.get<User>(`${this.usersUrl}/password/?password=${term}`).pipe(
      catchError(this.handleError<User>('searchPassword'))
    );
  }

  getShoppingCart(user: User): Observable<Array<Product>>{
    return this.http.get<Array<Product>>(`${this.usersUrl}/cart/?username=${user.username}`).pipe(
      catchError(this.handleError<Array<Product>>('getShoppingCart'))
    );
  }

  getWishList(user: User): Observable<Array<Product>>{
    return this.http.get<Array<Product>>(`${this.usersUrl}/wishlist/?username=${user.username}`).pipe(
      catchError(this.handleError<Array<Product>>('getWishList'))
    );
  }

  updateUser(user: User): Observable<User>{
    return this.http.post<User>(`${this.usersUrl}/addItem/`, user, this.httpOptions).pipe(
      catchError(this.handleError<User>('addItemToCart')));
  }
}
