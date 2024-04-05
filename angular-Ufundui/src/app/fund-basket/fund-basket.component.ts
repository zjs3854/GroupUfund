import { Component, OnInit } from '@angular/core';
import { FundBasketService } from '../fundbasket.service';
import { Need } from '../need';
import { AuthenService } from '../authen.service';
import { UfundControllerService } from '../ufund-controller.service';
import { Router } from '@angular/router';
import { Helper } from '../helper';
import { UserItem } from '../useritem';
import { HelperService } from '../helper.service';
import { Subscription } from 'rxjs';
import { waitForAsync } from '@angular/core/testing';
@Component({
  selector: 'app-fund-basket',
  templateUrl: './fund-basket.component.html',
  styleUrls: ['./fund-basket.component.css']
})

export class FundBasketComponent implements OnInit {
  need: Need = {id:0,name:"",description:"",quantity:0,price:0,notw:false, expireDate:null};
  fundingBasketNeeds: Need[] = new Array<Need>;
  helper: Helper = {username:"", password:"", fundBasket:new Array<UserItem>};
  basket: UserItem[] = new Array<UserItem>;
  constructor(private helperService : HelperService, private fundBasketService: FundBasketService, private authenService: AuthenService, private ufundService: UfundControllerService, private router: Router) { }
  //populates the helper and basket on init
  ngOnInit(): void {
    console.log(`Funding Basket OnInit`);
    this.authenService.loadUser();
    this.load();
  }

  //calls the fundBasket and needs
  load(): void {
    this.authenService.getUser().subscribe( async (helper)=> {
    this.helper= helper;
    console.log(`loading funding basket...`);
    console.log(`fundBasket user loaded\n Username: ${this.helper.username}\nPassword: ${this.helper.password}\nBasket: ${this.helper.fundBasket}`);
    this.loadNeeds();
    setTimeout(()=>{
      this.loadFundBasket();
    },100);
    console.log(`loaded items: ${this.helper.fundBasket}`);
    });
  }

  getItemByID(id:number): UserItem|null {
    let item: UserItem | null = null;
    if(this.basket && this.basket.length>0) {
      let index=0;
      while(index<this.basket.length) {
        if(this.basket[index].id==id) {
          item = this.basket[index];
        }
        index+=1;
      }
    }
    return item;
  }

  getBasketPrice(id: number,quantity: number): number {
    if(this.fundingBasketNeeds!=null && this.fundingBasketNeeds.length>0) {
      let index = 0;
      while(index<this.fundingBasketNeeds.length) {
        if(this.fundingBasketNeeds[index].id==id) {
          return this.fundingBasketNeeds[index].price * quantity;
        }
        index+=1;
      }
    }
    return 0;
  }

  //sets to helper
  loadFundBasket(): void {
    if(this.helper.fundBasket && this.helper.fundBasket.length>0) {
      let index1=0;
        while(index1 < this.helper.fundBasket.length) {
          let contains=false;
          let index2=0;
            while(index2 < this.fundingBasketNeeds.length) {
              if(this.helper.fundBasket[index1].id==this.fundingBasketNeeds[index2].id) {
                contains=true;
              }
              index2+=1;
            }
            if(contains==false) {
              this.fundBasketService.removeFromFundBasket(this.helper,{id:this.helper.fundBasket[index1].id} as Need).subscribe();
            }
            index1+=1;
        }
    }
    this.basket=this.helper.fundBasket;
  }

  //returns need based on ID
  getNeed(id: number): Need {
    return this.fundBasketService.getNeedOfBasket(id);
  }

  //return the needs of the basket based on the 
  loadNeeds(): void {
    this.fundingBasketNeeds = this.fundBasketService.getNeedsOfBasket(this.helper);
  }

  //sets the quantity of the basket item to specific number
  setQuantity(need: Need, quantity: string): void {
    //checks if the need is non-negative
    if(parseInt(quantity)>0) {
    //checks for null basket
      if(this.basket!=null && this.basket.length>0) {
        if(need.quantity<parseInt(quantity)){
          if(!confirm("This exceeds the amount needed, is this okay?"))
          {
            return;
          }
        }
          this.fundBasketService.setQuantity(this.helper,need,parseInt(quantity)).subscribe(helper=>this.helper=helper);
      }
    }
    else {
      alert("ERROR: Quantity invalid");
    }
  }

  clearFundBasket(): void {
    this.fundBasketService.clearFundBasket(this.helper).subscribe(helper=>this.helper=helper);
  }

  basketCheckout(): void {
    //checks for null
    if(this.fundingBasketNeeds!=null && this.fundingBasketNeeds.length>0) {
      //confirmation to checkout
      confirm("OK to check out?");
      //loops through basket
      var index=0;
      while(index<this.fundingBasketNeeds.length) {
        //gets a need
        let item: UserItem | null = this.getItemByID(this.fundingBasketNeeds[index].id);
        if(item) {
           //updates the quantity
           this.fundingBasketNeeds[index].quantity-=this.basket[index].quantity;
           if(this.fundingBasketNeeds[index].quantity<0)
           {
            if(!confirm("This need has alread been met, continue checking out?"))
            {
              return;
            }
           }
           this.ufundService.updateNeed(this.fundingBasketNeeds[index]).subscribe();
           index+=1;
        }
        else {
          alert("ERROR: Checkout with invalid need!");
          return;
        }
        }
    }
    if(index=this.basket.length) {
      alert("Checkout Completed!");
      this.fundBasketService.clearFundBasket(this.helper).subscribe(()=>this.router.navigate(['/helper/']));
    }
    else {
      alert("ERROR: Basket empty!");
    }

  }

  absoluteDisplay(qty: number): number {
    return Math.abs(qty);
  }

  isExcessDisplay(need: Need): String {
    if(this.isExcess(need)) {
      return "Need Met!"
    }
    else {
      return "";
    }
  }

  isExcess(need: Need): boolean {
    if(need.quantity<0) {
      return true;
    }
    else {
      return false;
    }
  }
  
  
  goToHelper(): void {
    this.router.navigate(['/helper']);
  }

}