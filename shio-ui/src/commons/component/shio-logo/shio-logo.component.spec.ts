import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ShioLogoComponent } from './shio-logo.component';

describe('ShioLogoComponent', () => {
  let component: ShioLogoComponent;
  let fixture: ComponentFixture<ShioLogoComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ShioLogoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShioLogoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
