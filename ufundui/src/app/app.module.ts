import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent }  from './app.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { CupboardComponent } from './cupboard/cupboard.component';

@NgModule({
  imports:      [ BrowserModule, FormsModule, CommonModule, AppComponent, AppRoutingModule, HttpClientModule,],
  declarations: [ AppComponent,CupboardComponent],
  bootstrap:    [ ]
})
export class AppModule { }