import { Component, OnInit } from '@angular/core';
import { TokenService } from '../services/token.service';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.css']
})
export class NavigationBarComponent implements OnInit {

  hasUser: Boolean;
  roles: String[] = [];

  constructor(private tokenService: TokenService) {
    this.tokenService.eventEmitter.subscribe(
      () => {
        this.hasUser = (this.tokenService.getToken() === null) ? false : true;
        this.roles = (this.tokenService.getToken() === null) ? [] : this.tokenService.getAuthorities();
      }
   );
   }

  ngOnInit() {
    this.hasUser = (this.tokenService.getToken() === null) ? false : true;
    this.roles = (this.tokenService.getToken() === null) ? [] : this.tokenService.getAuthorities();
  }

  logOut() {
    this.tokenService.signOut();
  }

}
