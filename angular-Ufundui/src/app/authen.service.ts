import { Injectable, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Helper } from './helper';
import { Observable, catchError, tap } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { HelperService } from './helper.service';
import { UserItem } from './useritem';

@Injectable({
  providedIn: 'root'
})

export class AuthenService{
  //the user which all things past login are based on, particularly in helper
  private user: Helper= {username:"", password:"", fundBasket:[]};
  constructor(private router: Router ,private http: HttpClient,private helperservice: HelperService) {}


  //login runs a GET command using tryUser() returns an observable
  login(username: string, password: string): Observable<Helper> {
    this.saveUser(username,password)
    return this.tryUser(this.user.username,this.user.password);
  }

  //uses setuser, but leaves the fields blank for the next login
  signOut(): void {
    this.saveUser("","");
  }

  //POST command using helperService to add the registered user
  register(username: string, password: string): void {
    var helper : Helper = {username:username, password:password, fundBasket: new Array<UserItem>}
    this.helperservice.addHelper(helper).subscribe();
  }

  getUser(): Observable<Helper> {
    console.log(`getuser: Username: ${this.user.username} Password ${this.user.password}`);
    return this.helperservice.getHelper(this.user.username,this.user.password);
  }

  //GET command using helperService
  tryUser(username:string, password:string): Observable<Helper> {
    this.user.username=username;
    this.user.password=password;
    this.getUser().subscribe((user)=>{
      console.log(`tryUser: Username: ${user.username} Password ${user.password} Basket: ${user.fundBasket}`)
      this.saveUser(user.username,user.password);
    });
    return this.helperservice.getHelper(username,password);
  }

  //saves the user's username and password to localstorage to be used outside the service
  saveUser(username : string, password:string): void {
    console.log(`saving user...`);
    localStorage.setItem("username",username);
    localStorage.setItem("password",password);
    this.user.username=username;
    this.user.password=password;
  }

  //gets the user that had been saved
  loadUser(): void {
    var username = localStorage.getItem("username");
    var password = localStorage.getItem("password");
    console.log(`loading user: username=${username} password=${password}`);
    if(username!=null && password!=null) {
      console.log(`getting user...`);
      this.tryUser(username,password).subscribe(helper=>this.user=helper);
    }
  }
  
  //allows a new helper to be given
  exportUser(): Observable<Helper> {
    return this.tryUser(this.user.username,this.user.password);
  }

}