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
import { ArticleComponent } from './article/article.component';
import { TaskListComponent } from './task-list/task-list.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTabsModule } from '@angular/material/tabs';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { UserFormComponent } from './user-form/user-form.component';
import { SuccessComponent } from './success/success.component';
import { ErrorComponent } from './error/error.component';
import { FailedComponent } from './failed/failed.component';
import { AllMagazinesComponent } from './all-magazines/all-magazines.component';
import { MagazineComponent } from './magazine/magazine.component';
import { IssueComponent } from './issue/issue.component';
import {MatCardModule} from '@angular/material/card';
import { AllIssuesComponent } from './all-issues/all-issues.component';
import { AllScientificPapersComponent } from './all-scientific-papers/all-scientific-papers.component';
import { ScientificPaperComponent } from './scientific-paper/scientific-paper.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { PurchasedItemsComponent } from './purchased-items/purchased-items.component';
import { MagazineIssuesEditorComponent } from './magazine-issues-editor/magazine-issues-editor.component';

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
      HomeComponent,
      ArticleComponent,
      TaskListComponent,
      UserFormComponent,
      SuccessComponent,
      ErrorComponent,
      FailedComponent,
      AllMagazinesComponent,
      MagazineComponent,
      IssueComponent,
      AllIssuesComponent,
      IssueComponent,
      AllScientificPapersComponent,
      ScientificPaperComponent,
      ShoppingCartComponent,
      PurchasedItemsComponent,
      MagazineIssuesEditorComponent
   ],
   imports: [
      BrowserModule,
      AppRoutingModule,
      HttpClientModule,
      FormsModule,
      NgMultiSelectDropDownModule.forRoot(),
      BrowserAnimationsModule,
      MatTabsModule,
      AngularFontAwesomeModule,
      MatCardModule
   ],
   providers: [
      httpInterceptorProviders
   ],
   bootstrap: [
      AppComponent
   ]
})
export class AppModule { }
