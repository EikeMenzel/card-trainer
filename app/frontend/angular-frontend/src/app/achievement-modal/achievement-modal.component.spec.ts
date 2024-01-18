import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AchievementModalComponent } from './achievement-modal.component';

describe('AchievementModalComponent', () => {
  let component: AchievementModalComponent;
  let fixture: ComponentFixture<AchievementModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AchievementModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AchievementModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
