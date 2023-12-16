
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EditCardViewComponent } from './edit-card-view.component';

describe('EditCardBasicComponent', () => {
  let component: EditCardViewComponent;
  let fixture: ComponentFixture<EditCardViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditCardViewComponent ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCardViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
