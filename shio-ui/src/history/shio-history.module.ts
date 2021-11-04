import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MomentModule } from 'ngx-moment';
import { ShioCommitPageComponent } from './shio-commit-page/shio-commit-page.component';
import { ShHistoryService } from './service/history.service';
import { ShioRepositoryModule } from 'src/repository/shio-repository.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ShioCommonsModule } from 'src/commons/shio-commons.module';

@NgModule({
  declarations: [ShioCommitPageComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MomentModule,
    ShioCommonsModule,
    ShioRepositoryModule
  ],
  providers: [
    ShHistoryService
  ]
})
export class ShioHistoryModule { }
