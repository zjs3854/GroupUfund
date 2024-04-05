import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenService } from '../authen.service';
import { Observable } from 'rxjs';
import { Helper } from '../helper';

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrl: './log-in.component.css'
})
export class LogInComponent implements OnInit{
  username: string="";
  password: string="";
  constructor(private authService: AuthenService, private router: Router) { }
  
  
  ngOnInit(): void {
    this.authService.signOut();
  }

  //Uses AuthenService to check for a matching username and password
  login(): void {
    this.authService.login(this.username, this.password).subscribe((helper)=> {
      if(helper!=null && (helper.username != "" || helper.password != "")) {
        this.username=helper.username;
        this.password=helper.password;
        this.route();
      }
      else 
      {
      alert('Invalid username or password');
      } 
    });
  }
  
    route(): void {
      if(this.username=="admin") {
        this.router.navigate(['/cupboard']);
      }
      else {
        this.router.navigate(['/helper']);
      }
    }
}
