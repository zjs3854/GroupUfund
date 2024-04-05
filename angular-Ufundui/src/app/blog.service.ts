import { Injectable } from '@angular/core';
import { Post } from './post'
import { Observable, of } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BlogService {
  private blogUrl = 'http://localhost:8080/posts';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient,private messageService: MessageService) { }

  /** Log a BlogService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`U-fundService: ${message}`);
  }

  getPosts(): Observable<Post[]> {
    console.log("getting posts")
    return this.http.get<Post[]>(this.blogUrl).pipe(tap(_=> this.log('fetched posts')));
  }

  /** GET post by id. Will 404 if id not found */
  getPost(id: number): Observable<Post> {
    const url = `${this.blogUrl}/${id}`;
    return this.http.get<Post>(url).pipe(
      tap(_ => this.log(`fetched need id=${id}`)),
      catchError(this.handleError<Post>(`getPost id=${id}`))
    );
  }

  /** GET post by username. Will 404 if id not found */
  getPostsByName(name: string): Observable<Post[]> {
    const url = `${this.blogUrl}/?username=${name}`;
    return this.http.get<Post[]>(url).pipe(
      tap(_ => this.log(`fetched need name=${name}`)),
      catchError(this.handleError<Post[]>(`getPost id=${name}`))
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

  /** PUT: update the post on the server */
  updatePost(post: Post): Observable<any> {
    return this.http.put(this.blogUrl, post, this.httpOptions).pipe(
      tap(_ => this.log(`updated need id=${post.id}`)),
      catchError(this.handleError<any>('updateNeed'))
    );
  }

  /** POST: add a new need to the server */
  addPost(post: Post): Observable<Post> {
    return this.http.post<Post>(this.blogUrl, post, this.httpOptions).pipe(
      tap((newPost: Post) => this.log(`added post w/ id=${newPost.id}`)),
      catchError(this.handleError<Post>('addPost'))
    );
  }

  

  /** DELETE: remove a post from the server */
  deletePost(id: Number): Observable<any> {
    const url = `${this.blogUrl}/${id}`;
    return this.http.delete<Post>(url);
   }

}
