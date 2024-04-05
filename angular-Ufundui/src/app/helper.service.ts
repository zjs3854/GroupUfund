import { Injectable } from '@angular/core';
import { Need } from './need'
import { Observable, of } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Helper } from './helper';

@Injectable({
  providedIn: 'root'
})
export class HelperService {
  private userUrl = 'http://localhost:8080/users';
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient,private messageService: MessageService) { }

  //creates a single user with the username and password provided, used for register
  addHelper(helper: Helper): Observable<Helper> {
    console.log(`helper username: ${helper.username} password: ${helper.password}`);
    return this.http.post<Helper>(this.userUrl, helper, this.httpOptions).pipe(
      tap((newBasket: Helper)=>alert(`User added: ${helper.username}\n Enjoy!`)),
      catchError(this.handleError<Helper>(`addHelper`))
      );
  }

  //**GET a single user, used for login
  getHelper(username: string ,password:string): Observable<Helper> {
    const url = `${this.userUrl}/?name=${username}&password=${password}`;
    return this.http.get<Helper>(url).pipe(
      tap(_=>this.log(`retrived helper username=${username}`)),
    catchError(this.handleError<Helper>(`getHelper id=${username}`))
    );
  }

  /** PUT: Update the Helper, in particular this is used for the fundbasket */
  updateHelper(helper: Helper): Observable<any> {
    console.log(`updating helper: ${helper}`);
    return this.http.put(this.userUrl,helper,this.httpOptions).pipe(
      tap(_ => this.log(`updated basket username=${helper.username}`)),
      catchError(this.handleError<any>('updateBasket'))
    );
  }
  
  log(message: string) {
    this.messageService.add(`HelperService: ${message}`);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
