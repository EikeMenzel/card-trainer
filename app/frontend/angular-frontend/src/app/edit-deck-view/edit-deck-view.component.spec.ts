import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDeckViewComponent } from './edit-deck-view.component';

describe('EditDeckViewComponent', () => {
  let component: EditDeckViewComponent;
  let fixture: ComponentFixture<EditDeckViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditDeckViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditDeckViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
