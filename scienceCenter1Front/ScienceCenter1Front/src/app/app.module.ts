import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { RegistrationComponent } from './registration/registration.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';
import { HttpModule } from '@angular/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material';
import { CheckMailComponent } from './check-mail/check-mail.component';
import { SuccessComponent } from './success/success.component';
import { WaitAdminConfirmationComponent } from './wait-admin-confirmation/wait-admin-confirmation.component';
import { LoginComponent } from './login/login.component';
import { AllMyTasksComponent } from './all-my-tasks/all-my-tasks.component';
import {  RequestLogInterceptor } from './authentication/AuthInterceptor';
import { TaskDetailsComponent } from './task-details/task-details.component';
import { CreateMagazineComponent } from './create-magazine/create-magazine.component';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { HomePageComponent } from './home-page/home-page.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { CreateAdminComponent } from './create-admin/create-admin.component';
import { CreateEditorComponent } from './create-editor/create-editor.component';
import { ProcessingTextComponent } from './processing-text/processing-text.component';
import { FailedComponent } from './failed/failed.component';
import { ErrorComponent } from './error/error.component';
import { SuccessPaymentComponent } from './success-payment/success-payment.component';
import { PublicationsComponent } from './publications/publications.component';
import { ArticleComponent } from './article/article.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { PurchasedItemsComponent } from './purchased-items/purchased-items.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    RegistrationComponent,
    CheckMailComponent,
    SuccessComponent,
    WaitAdminConfirmationComponent,
    LoginComponent,
    AllMyTasksComponent,
    TaskDetailsComponent,
    CreateMagazineComponent,
    HomePageComponent,
    AdminPanelComponent,
    CreateAdminComponent,
    CreateEditorComponent,
    ProcessingTextComponent,
    FailedComponent,
    ErrorComponent,
    SuccessPaymentComponent,
    PublicationsComponent,
    ArticleComponent,
    ShoppingCartComponent,
    PurchasedItemsComponent,
  ],
  imports: [
    HttpClientModule,
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    HttpModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatTabsModule,
    NgMultiSelectDropDownModule.forRoot()
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: RequestLogInterceptor,
    multi: true
  }
 ],
  bootstrap: [AppComponent]
})
export class AppModule { }
