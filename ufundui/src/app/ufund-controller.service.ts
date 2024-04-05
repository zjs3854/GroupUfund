import { Injectable } from '@angular/core';
import { Need } from './need'
// import { NEEDS } from './mock-needs';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UfundControllerService {
  private needsUrl = 'http://localhost:8080/needs';

  constructor(private http: HttpClient,private messageService: MessageService) { }

  getNeeds(): Observable<Need[]>{
    return this.http.get<Need[]>(this.needsUrl).pipe(tap(_ => this.log('fetched needs')),catchError(this.handleError<Need[]>('getNeeds', [])));
  }
  /** Log a NeedService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`U-fundService: ${message}`);
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