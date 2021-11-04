import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShioPostPageComponent } from './component/shio-post-page/shio-post-page.component';
import { ShioPostSettingsPageComponent } from './component/shio-post-settings-page/shio-post-settings-page.component';
import { ShioPostTabComponent } from './component/shio-post-tab/shio-post-tab.component';
import { ShioPostTabsComponent } from './component/shio-post-tabs/shio-post-tabs.component';
import { ShPostService } from './service/post.service';
import { EditorModule, TINYMCE_SCRIPT_SRC } from '@tinymce/tinymce-angular';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { AceEditorModule } from 'ace-editor-ng9';
import { ShioPostRoutingModule } from './shio-post-routing.module';
import { ShioCommonsModule } from 'src/commons/shio-commons.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ShioWidgetModule } from 'src/widget/shio-widget.module';

@NgModule({
  declarations: [
    ShioPostPageComponent,
    ShioPostSettingsPageComponent,
    ShioPostTabComponent,
    ShioPostTabsComponent

  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    AceEditorModule,
    EditorModule,
    CKEditorModule,
    ShioCommonsModule,
    ShioPostRoutingModule,
    ShioWidgetModule
  ],
  providers: [
    ShPostService,
    { provide: TINYMCE_SCRIPT_SRC, useValue: 'tinymce/tinymce.min.js' }
  ],
})
export class ShioPostModule { }
