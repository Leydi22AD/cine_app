import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormFuncionComponent } from './form-funcion.component';

describe('FormFuncionComponent', () => {
  let component: FormFuncionComponent;
  let fixture: ComponentFixture<FormFuncionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormFuncionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormFuncionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
