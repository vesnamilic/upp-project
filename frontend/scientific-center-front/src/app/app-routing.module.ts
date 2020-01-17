import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { LoginComponent } from './login/login.component';
import { TaskService } from './services/task.service';
import { AdministratorTasksComponent } from './administrator-tasks/administrator-tasks.component';
import { UserConfirmationFormComponent } from './user-confirmation-form/user-confirmation-form.component';
import { MagazineFormComponent } from './magazine-form/magazine-form.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'register',
    component: RegistrationComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'tasks',
    component: AdministratorTasksComponent
  },
  {
    path: 'tasks/:id',
    component: UserConfirmationFormComponent
  },
  {
    path: 'magazine',
    component: MagazineFormComponent
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

