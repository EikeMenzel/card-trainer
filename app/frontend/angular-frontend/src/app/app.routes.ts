import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {DeckListViewComponent} from "./deck-list-view/deck-list-view.component";
import {DeckViewComponent} from "./deck-view/deck-view.component";
import {RegisterSuccessfulComponent} from "./register-successful/register-successful.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {ResetPasswordComponent} from "./reset-password/reset-password.component";
import {EditDeckViewComponent} from "./edit-deck-view/edit-deck-view.component";
import {EditCardViewComponent} from "./edit-card-view/edit-card-view.component";
import {UserProfileComponent} from "./user-profile/user-profile.component";
import {LearnCardViewComponent} from "./learn-card-view/learn-card-view.component";



export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register-successful', component: RegisterSuccessfulComponent},
  {path: 'deck/:deck-id', component: DeckViewComponent},
  {path: 'deck/:deck-id/edit', component: EditDeckViewComponent},
  {path: 'deck/:deck-id/card/:card-id/edit', component: EditCardViewComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'reset-password', component: ResetPasswordComponent},
  {path: '', component: DeckListViewComponent},
  {path: 'deck/:deck-id/learn', component: LearnCardViewComponent},
  {path:"suc", component: RegisterSuccessfulComponent},
  {path: 'profile', component: UserProfileComponent},
  { path: '**', redirectTo: "" },
];
