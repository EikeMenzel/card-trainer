import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, Params} from "@angular/router";
import {BasePageComponent} from "../base-page/base-page.component";

@Component({
  selector: 'app-deck-view',
  standalone: true,
  imports: [CommonModule, BasePageComponent],
  templateUrl: './deck-view.component.html',
  styleUrl: './deck-view.component.css'
})
export class DeckViewComponent {

  public deckId: string | null = "";

  private activatedRoute: ActivatedRoute;
  constructor(activatedRoute: ActivatedRoute) {
    this.activatedRoute = activatedRoute;
  }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe((params) => {
      this.deckId = params.get('deck-id');
    });

    console.log(this.deckId);
  }
}
