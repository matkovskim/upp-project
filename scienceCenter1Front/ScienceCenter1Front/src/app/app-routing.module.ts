import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { CheckMailComponent } from './check-mail/check-mail.component';
import { SuccessComponent } from './success/success.component';
import { WaitAdminConfirmationComponent } from './wait-admin-confirmation/wait-admin-confirmation.component';
import { LoginComponent } from './login/login.component';
import { AllMyTasksComponent } from './all-my-tasks/all-my-tasks.component';
import { TaskDetailsComponent } from './task-details/task-details.component';
import { CreateMagazineComponent } from './create-magazine/create-magazine.component';
import { HomePageComponent } from './home-page/home-page.component';

const routes: Routes = [
  {
    path: '',
    component: HomePageComponent
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
    component: AllMyTasksComponent
  },
  {
    path: 'checkMail',
    component: CheckMailComponent
  },
  {
    path: 'success',
    component: SuccessComponent
  },
  {
    path: 'waitAdminConfirmation',
    component: WaitAdminConfirmationComponent
  },
  {
    path: 'taskDetails/:taskId',
    component: TaskDetailsComponent
  },
  {
    path: 'createMagazine',
    component: CreateMagazineComponent
  }
];

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
})
export class AppRoutingModule { }
