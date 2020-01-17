import { Component, OnInit } from '@angular/core';
import { TokenService } from '../services/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  hasUser: Boolean;

  constructor(private tokenService: TokenService) {
    this.tokenService.eventEmitter.subscribe(
      () => {
        this.hasUser = (this.tokenService.getToken() === null) ? false : true;
      }
   );
  }

  ngOnInit() {
    this.hasUser = (this.tokenService.getToken() === null) ? false : true;
  }

}
