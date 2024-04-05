import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { LayoutModule } from '@angular/cdk/layout'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CupboardComponent } from './cupboard/cupboard.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { FormsModule } from '@angular/forms';
import { LogInComponent } from './log-in/log-in.component';
import { RegisterComponent } from './register/register.component';
import { FundBasketComponent } from './fund-basket/fund-basket.component';
import { HelperComponent } from './helper/helper.component';
import { NeedOfTheWeekComponent } from './need-of-the-week/need-of-the-week.component';
import { BlogComponent } from './blog/blog.component';


@NgModule({
  declarations: [
    AppComponent,
    CupboardComponent,
    NeedDetailComponent,
    LogInComponent,
    RegisterComponent,
    FundBasketComponent,
    HelperComponent,
    NeedOfTheWeekComponent,
    BlogComponent,
    
   
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    FormsModule,
    HttpClientModule, 
    LayoutModule
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
