import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {DeckListViewComponent} from "./deck-list-view/deck-list-view.component";
import {DeckViewComponent} from "./deck-view/deck-view.component";
import {RegisterSuccessfulComponent} from "./register-successful/register-successful.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";


export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register-successful', component: RegisterSuccessfulComponent},
  {path: 'deck/:deck-id', component: DeckViewComponent},
  {path: '', component: DeckListViewComponent},
  {path:"suc", component: RegisterSuccessfulComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  { path: '**', redirectTo: "" },
];
