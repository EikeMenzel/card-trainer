import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeckListViewComponent } from './deck-list-view.component';

describe('DeckviewComponent', () => {
  let component: DeckListViewComponent;
  let fixture: ComponentFixture<DeckListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeckListViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeckListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
