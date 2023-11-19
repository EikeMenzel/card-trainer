import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {DeckListViewComponent} from "./deck-list-view/deck-list-view.component";
import {DeckViewComponent} from "./deck-view/deck-view.component";

export const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'deck/:deck-id', component: DeckViewComponent},
  {path: '', component: DeckListViewComponent},
  { path: '**', redirectTo: "" },
];
