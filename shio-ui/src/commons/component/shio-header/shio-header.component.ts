import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '@app/_models';
import { UserService, AuthenticationService } from '@app/_services';

@Component({
  selector: 'shio-header',
  templateUrl: './shio-header.component.html'
})
export class ShioHeaderComponent implements OnInit {

  user: Observable<User>;

  constructor(private userService: UserService, private authenticationService: AuthenticationService) { }


  logout() {
    this.authenticationService.logout();
  }

  ngOnInit(): void {
    this.user = this.userService.getAll();
  }

}
