import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NeedDetailComponent } from './need-detail/need-detail.component';
import { CupboardComponent } from './cupboard/cupboard.component';
import { LogInComponent } from './log-in/log-in.component';
import { RegisterComponent } from './register/register.component';
import { FundBasketComponent } from './fund-basket/fund-basket.component';
import { HelperComponent } from './helper/helper.component';
import { BlogComponent } from './blog/blog.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'detail/:id', component: NeedDetailComponent },
  { path: 'cupboard', component: CupboardComponent },
  { path: 'login', component: LogInComponent},
  { path: 'register', component: RegisterComponent},
  { path: "fund-basket", component: FundBasketComponent},
  { path: 'helper', component: HelperComponent},
  { path: 'blog', component: BlogComponent},
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}