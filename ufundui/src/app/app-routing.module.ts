import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CupboardComponent } from './cupboard/cupboard.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {path: 'cupboard', component: CupboardComponent}
];

@NgModule({

  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
