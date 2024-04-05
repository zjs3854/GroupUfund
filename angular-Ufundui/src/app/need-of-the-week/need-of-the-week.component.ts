import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { UfundControllerService } from '../ufund-controller.service';

@Component({
  selector: 'app-need-of-the-week',
  templateUrl: './need-of-the-week.component.html',
  styleUrl: './need-of-the-week.component.css'
})
export class NeedOfTheWeekComponent implements OnInit{
  need: Need | undefined;
  needs: Need[] = [];
  selectedNumber: number = 1;
  numbers: number[] = Array.from({length: 30}, (_, i) => i + 1);
  
  constructor(
    private route: ActivatedRoute,
    private ufundService: UfundControllerService,
    private location: Location
  ) {}

  //start on initilization
  ngOnInit(): void {
    this.getNeeds();
    this.need=this.getCurrentNeed()
    
  }

  //GET the whole needs list
  getNeeds(): void {
    this.ufundService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }

  //gets the current need
  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.ufundService.getNeed(id)
      .subscribe(need => this.need = need);
  }

  //checks for current need of the week and returns it
  getNeedOfTheWeek(): Need[] {
    return this.needs.filter(need => need.notw === true);
  }

  //  gets the current need
  getCurrentNeed(): Need | undefined{
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    return  this.needs.find(need => need.id === id);
  }

  // sets the current need of the week
  setNeedOfWeek(): void {
    this.need=this.getCurrentNeed();
    const currentDate = new Date(); // Get the current date
    const expirationDate = new Date(currentDate); // Create a copy of the current date
    expirationDate.setDate(expirationDate.getDate() + Number(this.selectedNumber));
    // check for the current need of the week
    if(this.need){
      this.need.notw=true;
      this.need.expireDate=expirationDate;
      this.ufundService.updateNeed(this.need)
      .subscribe(() => this.goBack());
    } 
  }

  // return back to previous page
  goBack(): void {
      this.location.back();
    }

  // remove from the need spotlight
  removeNeedOfWeek(): void {
    this.need=this.getCurrentNeed();
    if(this.need){
      this.need.notw=false;
      this.need.expireDate=null;
      this.ufundService.updateNeed(this.need)
      .subscribe(() => this.goBack());
    }
  }
  // return a boolean if the current need is the need of the week
  isnotw(): boolean{
    this.need=this.getCurrentNeed();
    return this.need?.notw===true;
  }
  
}
