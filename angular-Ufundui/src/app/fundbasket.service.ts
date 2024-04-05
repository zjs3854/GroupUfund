import { Injectable, OnInit } from '@angular/core';
import { MessageService } from './message.service';
import { UserItem } from './useritem';
import { Helper } from './helper';
import { AuthenService } from './authen.service';
import { Need } from './need';
import { UfundControllerService } from './ufund-controller.service';
import { HelperService } from './helper.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})


export class FundBasketService implements OnInit{
  private basketUrl = 'http://localhost:8080/users';
  private user: Helper = {username:"", password:"", fundBasket:[]};
  constructor( private ufundService: UfundControllerService, private helperService: HelperService, private authService:AuthenService , private messageService: MessageService) { }

  //this is fine lol
  ngOnInit(): void {
    this.authService.loadUser();
  }

  //adds the item to the helper's funding basket
  addToBasket(helper: Helper, need: Need): Observable<any> {
    //checks for null & len 0
    if(helper.fundBasket!=null && helper.fundBasket.length>0) {
      let index=0;
      while(index<helper.fundBasket.length) {
          if(helper.fundBasket[index].id == need.id) {
            console.log(`Overlap!`);
            alert("ERROR: Item is already in fundingBasket!");
            return this.helperService.updateHelper(helper);
          }
          index++;
      }
      //make item and push it
      let tempitem : UserItem = {id:need.id,quantity:1};
      helper.fundBasket.push(tempitem);
      console.log(`New Basket is pushing ${tempitem}\nHelpers new basket is ${helper.fundBasket}`);
      //update & return observabe
      return this.helperService.updateHelper(helper);
    }
    //null will add UserItem automatically
    else {
      //make item and push it
      let item : UserItem = {id:need.id,quantity:1};
      console.log(`adding new item id ${item.id}`);
      helper.fundBasket.push(item);
      console.log(`New Basket is pushing ${item}\nHelpers new basket is ${helper.fundBasket}`)
      //update and return observable
      return this.helperService.updateHelper(helper);
    }
  }

  //removes the item from the helper's funding basket
  removeFromFundBasket(helper: Helper, need: Need): Observable<any> {
    let changes=0;
    //checks for null & len 0
    if(helper.fundBasket!=null && helper.fundBasket.length>0) {
      //makes placeholder array and then iterates through
      let index=0;
      
      while(index<helper.fundBasket.length) {
        //checks for ID match
        if(helper.fundBasket[index] != null && helper.fundBasket[index].id != null && helper.fundBasket[index].id==need.id) {
          //skips over if match
          console.log(`removing item ${helper.fundBasket[index].id}`);
          helper.fundBasket.splice(index,1);
          index-=1;
          changes+=1;
        }
        index+=1;
      }
      //sets the placeholder to the new basket
      console.log(`Helpers new basket is ${helper.fundBasket}`);
      //calls update and returns observable
    }
    if(changes==0) { alert("ERROR: Item is not in the fundBasket!") };
      return this.helperService.updateHelper(helper);
  }

  //removes the item from the helper's funding basket
  clearFundBasket(helper: Helper): Observable<any> {
    if(helper!=null && helper.fundBasket!=null && helper.fundBasket.length>=0) {
      helper.fundBasket.splice(0,helper.fundBasket.length);
    }
    return this.helperService.updateHelper(helper);
  }

  //sets the quantity of the designated Useritem passed in
  setQuantity(helper: Helper,need: Need, quantity:number): Observable<any> {
    if(helper.fundBasket!=null && helper.fundBasket.length>0) {
    let index = 0;
      while(index<helper.fundBasket.length) {
        if(helper.fundBasket[index].id==need.id)
        {
          helper.fundBasket[index].quantity=quantity;
        }
        index+=1;
      }
    }
    return this.helperService.updateHelper(helper);
  }

  //return array of UserItems from the helper <REFACTOR>
  getItemsOfBasket(helper : Helper): UserItem[] {
    this.authService.loadUser();
    return this.user.fundBasket;
  }

  //returns one specific need of item that was given <REFACTOR>
  getNeedOfBasket(id: number): Need {
    let need : Need = {id:-1,name:"",description:"",quantity:-1,price:-1,notw:false,expireDate:null};
    this.ufundService.getNeed(id).subscribe(tempneed=>need=tempneed);
    return need;
  }
  
  //Retrieves the needs that correspond to the UserItems that exist in the helper passed <REFACTOR/EXAMINE?
  getNeedsOfBasket(helper: Helper): Need[] {
    this.authService.loadUser();
    var needs : Need[] = new Array<Need>;
    if(helper.fundBasket!=null && helper.fundBasket.length>0) {
      let index = 0;
      while(index<helper.fundBasket.length) {
        this.ufundService.getNeed(helper.fundBasket[index].id).subscribe((need)=> {
          if(need && need.name) {
            needs.push(need);
          }
        });
        index++;
      }
    }
    return needs;
  }
}
