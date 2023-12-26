import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LearnSessionResultComponent } from './learn-session-result.component';

describe('LearnSessionResultComponent', () => {
  let component: LearnSessionResultComponent;
  let fixture: ComponentFixture<LearnSessionResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LearnSessionResultComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LearnSessionResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
