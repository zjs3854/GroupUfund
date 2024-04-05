import { Injectable } from '@angular/core';
import { Need } from './need'
import { Observable, of } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UfundControllerService {
  private needsUrl = 'http://localhost:8080/needs';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient,private messageService: MessageService) { }

  getNeeds(): Observable<Need[]>{
    return this.http.get<Need[]>(this.needsUrl).pipe(tap(_ => this.log('fetched needs')),catchError(this.handleError<Need[]>('getNeeds', [])));
  }
  
  /** Log a NeedService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`U-fundService: ${message}`);
  }

  /** GET need by id. Return `undefined` when id not found */
  getNeedNo404<Data>(id: number): Observable<Need> {
    const url = `${this.needsUrl}/?id=${id}`;
    return this.http.get<Need[]>(url)
      .pipe(
        map(needs => needs[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? 'fetched' : 'did not find';
          this.log(`${outcome} need id=${id}`);
        }),
        catchError(this.handleError<Need>(`getNeed id=${id}`))
      );
  }

  /** GET need by id. Will 404 if id not found */
  getNeed(id: number): Observable<Need> {
    const url = `${this.needsUrl}/${id}`;
    return this.http.get<Need>(url).pipe(
      tap(_ => this.log(`fetched need id=${id}`)),
      catchError(this.handleError<Need>(`getNeed id=${id}`))
    );
  }
  
  //TODO: Fix 400 status error, formatting for URL or something is wrong
  /** GET need by name. Will 404 if id not found */
  getNeedsByName(name: string): Observable<Need[]> {
    const url = `${this.needsUrl}/?name=${name}`;
    return this.http.get<Need[]>(url).pipe(
      tap(_ => this.log(`fetched need name=${name}`)),
      catchError(this.handleError<Need[]>(`getNeed id=${name}`))
    );
  }

  /** PUT: update the Need on the server */
  updateNeed(need: Need): Observable<any> {
    return this.http.put(this.needsUrl, need, this.httpOptions).pipe(
      tap(_ => this.log(`updated need id=${need.id}`)),
      catchError(this.handleError<any>('updateNeed'))
    );
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

   /** POST: add a new need to the server */
   addNeed(need: Need): Observable<Need> {
    return this.http.post<Need>(this.needsUrl, need, this.httpOptions).pipe(
      tap((newNeed: Need) => this.log(`added need w/ id=${newNeed.id}`)),
      catchError(this.handleError<Need>('addNeed'))
    );
  }

   /** DELETE: remove a need from the server */
   deleteNeed(id: Number): Observable<any> {
    const url = `${this.needsUrl}/${id}`;
    return this.http.delete<Need>(url);
   }

   
}