import { Component, OnInit, Input } from '@angular/core';
import { ShSite } from 'src/repository/model/site.model';
import { ShHistoryService } from 'src/history/service/history.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'shio-repository-tabs',
  templateUrl: './shio-repository-tabs.component.html'
})
export class ShioRepositoryTabsComponent implements OnInit {
  @Input() shSite: ShSite;
  @Input() tabIndex: number;
  private commitCount: Observable<number>;
  constructor(private shHistoryService: ShHistoryService) {
    
   }

  ngOnInit(): void {
    this.commitCount = this.shHistoryService.countBySite(this.shSite.id);
  }

  getCommitCount(): Observable<number> {
    return this.commitCount;
  }

}
