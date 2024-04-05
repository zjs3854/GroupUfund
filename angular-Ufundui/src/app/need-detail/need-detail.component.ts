import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { UfundControllerService } from '../ufund-controller.service';

@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrl: './need-detail.component.css',
  
})
export class NeedDetailComponent implements OnInit{
  need: Need | undefined;

  constructor(
    private route: ActivatedRoute,
    private ufundService: UfundControllerService,
    private location: Location
  ) {}
  //retrieves need of the specific need
  ngOnInit(): void {
    this.getNeed();
  }
  
  //using the paramMap retrieves our specific need
  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.ufundService.getNeed(id)
      .subscribe(need => this.need = need);
  }

  //REMOVE using ufundservice
  deleteNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.ufundService.deleteNeed(id)
      .subscribe(() => this.goBack());
  }

  //routing back to the cupboard component
  goBack(): void {
    this.location.back();
  }

  //PUT for the need after checking for invalid values
  save(): void {
    if (this.need) {
      if(this.need.name && this.need.price>0.0 && this.need.quantity>0) {
      this.ufundService.updateNeed(this.need)
        .subscribe(() => this.goBack());
      }
      else {
        alert("Error: Invalid Input!")
      }
    }
}
  
}