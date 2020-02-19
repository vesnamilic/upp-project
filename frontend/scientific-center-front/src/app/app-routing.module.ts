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
import { TaskListComponent } from './task-list/task-list.component';
import { ScientificPaperService } from './services/scientific-paper.service';
import { ArticleComponent } from './article/article.component';
import { UserFormComponent } from './user-form/user-form.component';
import { ErrorComponent } from './error/error.component';
import { SuccessComponent } from './success/success.component';
import { FailedComponent } from './failed/failed.component';
import { MagazineComponent } from './magazine/magazine.component';
import { AllMagazinesComponent } from './all-magazines/all-magazines.component';
import { AllIssuesComponent } from './all-issues/all-issues.component';
import { AllScientificPapersComponent } from './all-scientific-papers/all-scientific-papers.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { PurchasedItemsComponent } from './purchased-items/purchased-items.component';
import { MagazineIssuesEditorComponent } from './magazine-issues-editor/magazine-issues-editor.component';

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
    component: TaskListComponent
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
    path: 'all-magazines',
    component: AllMagazinesComponent
  },
  {
    path: 'admin-pane',
    component: AdministratorTasksComponent
  },
  {
    path: 'scientific-paper',
    component: ArticleComponent
  }
  ,
  {
    path: 'register-editors',
    component: UserFormComponent
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  },
  {
    path: 'error',
    component: ErrorComponent
  },
  {
    path: 'success',
    component: SuccessComponent
  },
  {
    path: 'cancel',
    component: FailedComponent
  },
  {
    path: 'issues/:magazineId',
    component: AllIssuesComponent
  },
  {
    path: 'scientific-papers/:issueId',
    component: AllScientificPapersComponent
  },
  {
    path: 'shopping-cart',
    component: ShoppingCartComponent
  },
  {
    path: 'purchased-items',
    component: PurchasedItemsComponent
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

