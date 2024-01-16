import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PeekCardViewComponent } from './peek-learn-card-view.component';

describe('LearnCardBasicComponent', () => {
  let component: PeekCardViewComponent;
  let fixture: ComponentFixture<PeekCardViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeekCardViewComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(PeekCardViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
