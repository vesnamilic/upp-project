import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TokenService } from '../services/token.service';
import { LoginRequest } from '../authorization/loginRequest';
import { RegistrationService } from '../services/registration.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  token: String = null;

  constructor(private tokenService: TokenService, private registrationService: RegistrationService,
    private router: Router) { }

  ngOnInit() {
    this.token = this.tokenService.getToken();
    if (this.token != null) {
      this.router.navigateByUrl('/');
    }
  }

  onSubmit(form: NgForm) {
    const loginRequest = new LoginRequest(form.value['username'], form.value['password']);
    this.registrationService.attemtAuthentication(loginRequest).subscribe(
      data => {
        this.tokenService.saveDate(data.expiratonDate);
        this.tokenService.saveToken(data.token);
        this.tokenService.saveUsername(data.username);
        this.tokenService.saveAuthorities(data.authorities);
        this.token = data.token;
        this.router.navigateByUrl('/');

      },
      errors => {
        alert('ERROR');
      }
    );

  }

}
