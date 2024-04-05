import { Component, OnInit } from '@angular/core';
import {
  NgIf,
  NgFor,
  UpperCasePipe,
} from '@angular/common';
// import {FormsModule} from '@angular/forms';
// import { NEEDS } from '../mock-needs';
import { Need } from '../need';
import { UfundControllerService } from '../ufund-controller.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-cupboard',
  standalone: false,
  // imports: [
  //   FormsModule,
  //   NgIf,
  //   NgFor,
  //   UpperCasePipe,
  // ],
  templateUrl: './cupboard.component.html',
  styleUrl: './cupboard.component.css'
})
export class CupboardComponent implements OnInit{

  // selectedNeed?: Need;

  needs: Need[] = [];
  
  constructor( private ufundService: UfundControllerService,  private messageService: MessageService){}
  ngOnInit(): void {
    this.getNeeds();
  }
  // onSelect(need: Need): void{
  //   this.selectedNeed=need;
  //   this.messageService.add(`CupboardComponent: Selected need id=${need.id}`);
  // }
  getNeeds(): void {
    this.ufundService.getNeeds()
        .subscribe(needs => this.needs = needs);
  }
}
