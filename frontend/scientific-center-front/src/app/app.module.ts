import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { NavigationBarComponent } from './navigation-bar/navigation-bar.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { AdministratorTasksComponent } from './administrator-tasks/administrator-tasks.component';
import { httpInterceptorProviders } from './authorization/authInterceptor';
import { UserConfirmationFormComponent } from './user-confirmation-form/user-confirmation-form.component';
import { MagazineFormComponent } from './magazine-form/magazine-form.component';
import { DynamicFormComponent } from './dynamic-form/dynamic-form.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { HomeComponent } from './home/home.component';

@NgModule({
   declarations: [
      AppComponent,
      RegistrationComponent,
      LoginComponent,
      NavigationBarComponent,
      AdministratorTasksComponent,
      UserConfirmationFormComponent,
      MagazineFormComponent,
      DynamicFormComponent,
      UnauthorizedComponent,
      HomeComponent
   ],
   imports: [
      BrowserModule,
      AppRoutingModule,
      HttpClientModule,
      FormsModule,
      NgMultiSelectDropDownModule.forRoot()
   ],
   providers: [
      httpInterceptorProviders
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
