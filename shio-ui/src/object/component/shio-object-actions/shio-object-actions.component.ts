import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'shio-object-actions',
  templateUrl: './shio-object-actions.component.html'
})
export class ShioObjectActionsComponent implements OnInit {
  @Input() title: string;
  constructor() { }

  ngOnInit(): void {
  }

}
