import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShioLoginPageComponent } from './shio-login-page';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ShioCommonsModule } from 'src/commons/shio-commons.module';



@NgModule({
  declarations: [ShioLoginPageComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    ShioCommonsModule
  ]
})
export class ShioLoginModule { }
