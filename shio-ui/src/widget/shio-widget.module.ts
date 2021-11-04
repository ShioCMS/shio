import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ShioTextWidgetComponent } from './component/shio-text-widget/shio-text-widget.component';
import { ShioTextAreaWidgetComponent } from './component/shio-text-area-widget/shio-text-area-widget.component';

@NgModule({
  declarations: [
    ShioTextWidgetComponent,
    ShioTextAreaWidgetComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  exports: [
    ShioTextWidgetComponent,
    ShioTextAreaWidgetComponent
  ]
})
export class ShioWidgetModule { }
