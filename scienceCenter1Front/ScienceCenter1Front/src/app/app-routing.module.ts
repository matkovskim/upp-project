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
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { CreateAdminComponent } from './create-admin/create-admin.component';
import { CreateEditorComponent } from './create-editor/create-editor.component';
import { ProcessingTextComponent } from './processing-text/processing-text.component';
import { ErrorComponent } from './error/error.component';
import { FailedComponent } from './failed/failed.component';
import { SuccessPaymentComponent } from './success-payment/success-payment.component';
import { PublicationsComponent } from './publications/publications.component';

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
  },
  {
    path: 'adminPanel',
    component: AdminPanelComponent
  },
  {
    path: 'createAdmin',
    component: CreateAdminComponent
  },
  {
    path: 'createEditor',
    component: CreateEditorComponent
  },
  {
    path: 'processingText',
    component: ProcessingTextComponent
  },
  {
    path: 'error',
    component: ErrorComponent
  },
  {
    path: 'failed',
    component: FailedComponent
  },
  {
    path: 'successPayed',
    component: SuccessPaymentComponent
  },
  {
    path: 'publications/:magazineId',
    component: PublicationsComponent
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
