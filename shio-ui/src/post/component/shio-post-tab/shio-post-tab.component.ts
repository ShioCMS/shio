import { Component, OnInit, Input } from '@angular/core';
import { ShPost } from 'src/post/model/post.model';
import { ShPostAttr } from 'src/post/model/postAttr.model';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';

@Component({
  selector: 'shio-post-tab',
  templateUrl: './shio-post-tab.component.html'
})
export class ShioPostTabComponent implements OnInit {
  @Input() shPost: ShPost;
  @Input() tabIndex: number;
  @Input() currentTab: number;

  public chkEditor = ClassicEditor;
  public tinyMCEConfig = {

    base_url: '/tinymce',
    suffix: '.min',
    height: 300,
    menubar: false,
    plugins: [
      'advlist autolink lists link image charmap print preview anchor',
      'searchreplace visualblocks code fullscreen',
      'insertdatetime media table paste code help wordcount'
    ],
    toolbar:
      'undo redo | formatselect | bold italic backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | removeformat | help'
  };

  constructor() { }

  ngOnInit(): void {
  }
  getTabPostAttrs() {
    let filteredShPostAttrs: ShPostAttr[] = this.shPost.shPostAttrs.filter(
      shPostAttr => shPostAttr.tab === this.tabIndex);
    return filteredShPostAttrs;
  }
}
