import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { ApiPlaygroundComponent } from './api-playground.component';

describe('ApiPlaygroundComponent', () => {
  let component: ApiPlaygroundComponent;
  let fixture: ComponentFixture<ApiPlaygroundComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ApiPlaygroundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApiPlaygroundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
