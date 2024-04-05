import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Need } from '../need';
import { UfundControllerService } from '../ufund-controller.service';
import { MessageService } from '../message.service';
import { FundBasketService } from '../fundbasket.service';

@Component({
  selector: 'app-cupboard',
  templateUrl: './cupboard.component.html',
  styleUrl: './cupboard.component.css'
})
export class CupboardComponent implements OnInit{
  selectedNeed?: Need;
  needs: Need[] = [];
  searchedNeeds : Need[] = [];
  constructor( private ufundService: UfundControllerService,  private messageService: MessageService, private router: Router){}



  ngOnInit(): void {
    this.getNeeds();
  }

  //selectedNeed stuff
  onSelect(need: Need): void{
    this.selectedNeed=need;
    this.messageService.add(`CupboardComponent: Selected need id=${need.id}`);
  }

  //POST need after checking for invalid vals
  add(name: string, description: string, quantity_string: string, price_string: string): void {
    name = name.trim();
    description = description.trim();
    var quantity = parseInt(quantity_string);
    var price = parseFloat(price_string);
    if (!name || !quantity || !price) { alert("Error: invalid field(s)") }
    else if(quantity<1) { alert("Error: invalid quantity") }
    else if(price<0) { alert("Error: invalid price") }
    else {
    this.ufundService.addNeed({name, description, quantity, price } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
    }
  }
  
  //GET the whole needs list
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
  
  //GET a need specifically by ID (important for basket)
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

  //GET a need by Name (MVP searchbar)
  getNeedByName(name: string): void {
    if(name!="") {
    this.ufundService.getNeedsByName(name.toLowerCase())
      .subscribe(searchedNeeds => this.searchedNeeds=searchedNeeds)
    }
    else {
      alert("Error: empty string")
    }
  }

  //routes to login page
  signOut(): void {
    this.router.navigate(['/login']);
  }

  goToBlog(): void {
    this.router.navigate(['/blog']);
  }

  //checks if need of the week is true
  getNeedOfTheWeek(): Need[] {
    return this.needs.filter(need => need.notw === true);
  }
  
  // checks if there are any current needs in the Spotlight
  hasNeedOfWeek(): boolean {
    return this.needs.some(need => need.notw === true);
  }

  hasSearchedNeeds(): boolean {
    return (this.searchedNeeds.length>0)
  }
}
