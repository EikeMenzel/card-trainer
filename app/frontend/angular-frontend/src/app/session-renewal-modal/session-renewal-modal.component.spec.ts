import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionRenewalModalComponent } from './session-renewal-modal.component';

describe('SessionRenewalModalComponent', () => {
  let component: SessionRenewalModalComponent;
  let fixture: ComponentFixture<SessionRenewalModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SessionRenewalModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SessionRenewalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
