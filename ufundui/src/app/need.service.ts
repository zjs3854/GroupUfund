import { Injectable } from '@angular/core';
import { Need } from './need';
import { NEEDS } from './mock-needs';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
@Injectable({
  providedIn: 'root'
})
export class NeedService {
  
  getNeeds(): Observable<Need[]> {
    const needs = of(NEEDS);
    this.messageService.add('NeedService: fetched needs');
    return needs;
  }
  

  constructor(private messageService: MessageService) { }
}
