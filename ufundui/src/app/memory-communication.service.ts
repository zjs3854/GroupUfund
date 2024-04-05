import { Injectable } from '@angular/core';
import { InMemoryDbService, RequestInfo } from 'angular-in-memory-web-api';
import { Need } from './need';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MemoryCommunicationService implements InMemoryDbService{
  createDb()  {
    const cupboard = [
      [{"id":11,"name":"Wild fire relief","description":"Seeking monetary contributions to aid relief efforts for Californian wild fires","quantity":1,"price":0.1},{"id":12,"name":"Green Power Initiative","description":"Help the IceCaps company push for the creation of new renewable energy sources","quantity":1,"price":100.0},{"id":13,"name":"Natural forests preservations","description":"Seeking volunteers to help plant trees, PRICE=The amount of hours","quantity":100,"price":2.0},{"id":15,"name":"Researching better alternatives for batteries","description":"Funding a research group that is looking for alternative ways to create cheaper electric cars","quantity":12,"price":25.5},
      {"id":16,"name":"Oil Spill Animal Rescue","description":"Help the ducklings!","quantity":100,"price":4.0},
      {"id":17,"name":"Can Recycling Drive","description":"Donate your cans to a local boyscout troop for their derby cars!","quantity":50,"price":0.0}]
    ];
    return {cupboard};
  }
}
