import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { UfundControllerService } from '../ufund-controller.service';
import { MessageService } from '../message.service';
import { FundBasketService } from '../fundbasket.service';
import { AuthenService } from '../authen.service';
import { Helper } from '../helper';
import { UserItem } from '../useritem';
import { Observable, timeInterval } from 'rxjs';
import { HelperService } from '../helper.service';
// import { trigger, transition, animate, style } from '@angular/animations';

@Component({
  selector: 'app-helper',
  templateUrl: './helper.component.html',
  styleUrl: './helper.component.css',
  
})

export class HelperComponent implements OnInit{
  
  currentNeedIndex: number = 0;
  selectedNeed?: Need;
  needs: Need[] = [];
  searchedNeeds : Need[] = [];

  helper: Helper = {username:"",password:"", fundBasket: new Array<UserItem>};
 

  constructor(private ufundService: UfundControllerService,  private messageService: MessageService, private router: Router,private fundBasketService: FundBasketService, private authenService: AuthenService){}
  
  ngOnInit(): void {
    console.log(`ngOnInit...`);
    this.authenService.loadUser();
    setTimeout(()=>{
    this.load(), 100
    });
    setInterval(() => {
      this.showNextNeed();
    }, 3500); 
  }

  onSelect(need: Need): void{
    this.selectedNeed=need;
    this.messageService.add(`CupboardComponent: Selected need id=${need.id}`);
  }

  load(): void {
    console.log(`helper component loading...`);
    this.authenService.exportUser().subscribe((newHelper)=> {
      this.helper=newHelper;
      this.getNeeds();
    });
  }

  //returns a list of all needs and adds it to the array that we have on display 
  getNeeds(): void {
    this.ufundService.getNeeds()
        .subscribe(needs => {
          this.needs = needs;
          this.checkExpireDates();});
  }
  
  //checks the expired dates for all of the needs and acts accordingly
  checkExpireDates():void {
    const currentDate = new Date(); // get the current date for comparison
    this.needs.forEach(need => {
      if (need.expireDate && new Date(need.expireDate) < currentDate) {
        console.log(`Need with ID ${need.id} has expired.`);
        need.expireDate=null;
        need.notw=false;
        this.ufundService.updateNeed(need)
        .subscribe();
        console.log(`Removed Need with ID ${need.id} from spotlight.`);
      }
    });
  }

  // checks if there are any current needs in the Spotlight
  hasNeedOfWeek(): boolean {
    return this.needs.some(need => need.notw === true);
  }
  
  //returns needs when an id is passed in
  getNeedByID(textid: string): void {
    var id = parseInt(textid);
    if(id>0) {
    this.ufundService.getNeed(id)
      .subscribe(searchedNeed =>this.searchedNeeds=searchedNeed? [searchedNeed]:[]);
    }
    else {
      alert("Invalid ID: "+String(id))
    }
  }
  //returns needs when a name is passed in
  getNeedByName(name: string): void {
    if(name!="") {
    this.ufundService.getNeedsByName(name.toLowerCase())
      .subscribe(searchedNeeds => this.searchedNeeds=searchedNeeds)
    }
    else {
      alert("Error: empty string")
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

  //adds to the fundBasket
  addToFundBasket(need: Need): void {
    this.fundBasketService.addToBasket(this.helper,need).subscribe();
  }
  
  //removing from the basket a specific need
  removeFromFundBasket(need: Need): void {
    //just calls FBS
    this.fundBasketService.removeFromFundBasket(this.helper,need).subscribe(helper=>this.helper=helper);
    console.log(`# of items: ${this.helper.fundBasket.length}`);
  }

  goToFundBasket(): void {
    this.router.navigate(['/fund-basket']); // Navigate to the fund basket page
  }
  goToBlog(): void {
    this.router.navigate(['/blog']);
  }

  signOut(): void {
    this.authenService.signOut();
    this.router.navigate(['/login']);
  }

  hasNeeds(): boolean {
    return (this.needs.length>0);
  }

  hasSearchedNeeds(): boolean {
    return (this.searchedNeeds.length>0);
  }

  // Function to display the next need in the slideshow
  showNextNeed(): void {
    if (this.needs.length > 0) {
      let nextIndex = (this.currentNeedIndex + 1) % this.needs.length;
      // Find the next index where notw=true
      while (nextIndex !== this.currentNeedIndex && !this.needs[nextIndex].notw) {
        nextIndex = (nextIndex + 1) % this.needs.length;
      }
      this.currentNeedIndex = nextIndex;
    }
  }
}
