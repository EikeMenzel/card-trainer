import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LearnCardViewComponent } from './learn-card-view.component';

describe('LearnCardBasicComponent', () => {
  let component: LearnCardViewComponent;
  let fixture: ComponentFixture<LearnCardViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LearnCardViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LearnCardViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
