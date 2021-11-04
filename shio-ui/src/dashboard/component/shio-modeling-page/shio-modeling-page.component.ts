import { Component, OnInit } from '@angular/core';
import { ShPostType } from 'src/postType/model/postType.model';
import { User } from '@app/_models';
import { Observable } from 'rxjs';
import { UserService } from '@app/_services';
import { ShPostTypeService } from 'src/postType/service/postType.service';

@Component({
  selector: 'app-shio-modeling-page',
  templateUrl: './shio-modeling-page.component.html'
})
export class ShioModelingPageComponent implements OnInit {

  postTypes: ShPostType[];
  orderProp: string;
  loading = false;
  user: Observable<User>;
  constructor(private userService: UserService, private postTypeService: ShPostTypeService) {
    postTypeService.query().subscribe(postTypes => {
      this.postTypes = postTypes.sort((a, b) => {
        if (a.title < b.title) {
          return -1;
        }
        if (b.title < a.title) {
          return 1;
        }
        return 0;
      })
    });
    this.orderProp = 'name';

  }
  getPostTypes(): ShPostType[] {
    return this.postTypes;
  }

  ngOnInit() {
    this.loading = true;
    this.user = this.userService.getAll();
  }


}
