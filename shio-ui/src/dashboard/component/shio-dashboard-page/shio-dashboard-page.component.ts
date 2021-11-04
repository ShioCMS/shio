import { Component, OnInit } from '@angular/core';
import { ShSite } from 'src/repository/model/site.model';
import { User } from '@app/_models';
import { UserService } from '@app/_services';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { ShSiteService } from 'src/repository/service/site/site.service';
@Component({
  selector: 'shio-dashboard-page',
  templateUrl: './shio-dashboard-page.component.html'
})

export class ShioDashboardPageComponent implements OnInit {
  sites: ShSite[];
  orderProp: string;
  loading = false;
  user: Observable<User>;
  constructor(private userService: UserService, private siteService: ShSiteService, private router: Router) {
    
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
  getRouter() : Router {
    return this.router;
  }
}