import { Component, Input } from '@angular/core';
import { Need } from '../need';
import { NgIf, UpperCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-need-detail',
  standalone: true,
  imports: [FormsModule, NgIf, UpperCasePipe],
  templateUrl: './need-detail.component.html',
  styleUrl: './need-detail.component.css'
})



export class NeedDetailComponent {
  @Input() need?: Need;
}
