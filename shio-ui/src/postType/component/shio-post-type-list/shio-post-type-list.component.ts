import { Component, OnInit, Input } from '@angular/core';
import { ShPostType } from 'src/postType/model/postType.model';

@Component({
  selector: 'shio-post-type-list',
  templateUrl: './shio-post-type-list.component.html'
})
export class ShioPostTypeListComponent implements OnInit {
  @Input() shPostTypes: ShPostType[];
  constructor() { }

  ngOnInit(): void {
  }

}
