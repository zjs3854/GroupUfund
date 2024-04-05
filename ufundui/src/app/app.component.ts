import { CommonModule, NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup, FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NEEDS } from './mock-needs';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [CommonModule, FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'IceCaps UFund';

  public needsArray = new Array();
  public searchTerm = new String;

  needManager: FormGroup;
  showNeedManager = false;
  needsList: any;

  constructor(private fb: FormBuilder) {
    this.needManager = this.fb.group({
      name: [''],
      description: [''],
      quantity: [0],
      price: [0.0]
    });
  }

  toggleNeedCreation() {
    this.showNeedManager = !this.showNeedManager;
  }

  submitNeed() {
    console.log(this.needManager.value);
    // You can handle the form data here (e.g., send to a server)

    this.needsArray.push(this.needManager.value);
    this.needManager.reset();
  }

  deleteNeed(index: number) {
    this.needsArray.splice(index, 1); // Remove the element at the given index
  }

  get filteredNeeds() {
    // Filter needs based on search term
    return this.searchTerm ? this.needsArray.filter(need => need.name.toLowerCase().includes(this.searchTerm.toLowerCase())) : this.needsArray;
  }
}
