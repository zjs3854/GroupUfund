import { Component } from '@angular/core';
import { AuthenService } from '../authen.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  username: string = "";
  password: string = "";

  constructor(private authService: AuthenService, private router: Router) { }
  
  //takes the ngModel fields username and password and registers them.
  register(): void {
    if(this.username!="" && this.password!="") {
      this.authService.register(this.username, this.password);
    }
    else {
      alert("ERROR: Invalid username or password");
    }
    
  }

  //routes to the login page
  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}
