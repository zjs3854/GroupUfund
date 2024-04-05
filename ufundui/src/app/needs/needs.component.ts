import { Component } from '@angular/core';
import { NgFor, NgIf, UpperCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Need } from '../need';
import { NeedDetailComponent } from '../need-detail/need-detail.component';
import { NeedService } from '../need.service';
import { MessageService } from '../message.service';


@Component({
    selector: 'app-needs',
    standalone: true,
    templateUrl: './needs.component.html',
    styleUrl: './needs.component.css',
    imports: [FormsModule, NgFor, NgIf, UpperCasePipe, NeedDetailComponent]
})
export class NeedsComponent {

  needs: Need[] = [];

  selectedNeed?: Need;

  constructor(private needService: NeedService, private messageService: MessageService) {}

  //calls getNeeds via needService, and subscribes to the observable returned (will update when fields changed)
  getNeeds(): void {
    this.needService.getNeeds()
      .subscribe(needs => this.needs = needs);
  }
  
  //equates the need selected (via a button) to the selectedNeed variable
  onSelect(need: Need):void {
    this.selectedNeed = need;
    this.messageService.add('NeedsComponent: Selected need id=${need.id}');
  }

  //declares what is going to be run once the application is initialized, so far getNeeds!
  ngOnInit(): void {
    this.getNeeds();
    this.messageService.add('NeedsComponent: Initialized')
  }
}
