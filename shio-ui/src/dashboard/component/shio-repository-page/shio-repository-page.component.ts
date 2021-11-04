import { Component, OnInit } from '@angular/core';
import { ShSite } from 'src/repository/model/site.model';
import { Observable } from 'rxjs';
import { User } from '@app/_models';
import { UserService } from '@app/_services';
import { ShSiteService } from 'src/repository/service/site/site.service';

@Component({
  selector: 'app-shio-repository-page',
  templateUrl: './shio-repository-page.component.html'
})
export class ShioRepositoryPageComponent implements OnInit {

  sites: ShSite[];
  orderProp: string;
  loading = false;
  user: Observable<User>;
  constructor(private userService: UserService, siteService: ShSiteService) {
    siteService.query().subscribe(sites => {
      this.sites = sites;
    });
    this.orderProp = 'name';
  }
  getSites(): ShSite[] {
    return this.sites;
  }

  ngOnInit() {
    this.loading = true;
    this.user = this.userService.getAll();
  }
}
