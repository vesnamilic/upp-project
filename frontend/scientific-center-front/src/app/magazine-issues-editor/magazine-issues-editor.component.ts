import { Component, OnInit } from '@angular/core';
import { IssueService } from '../services/issue.service';

@Component({
  selector: 'app-magazine-issues-editor',
  templateUrl: './magazine-issues-editor.component.html',
  styleUrls: ['./magazine-issues-editor.component.css']
})
export class MagazineIssuesEditorComponent implements OnInit {

  issues = [];
  constructor(private issueService: IssueService) { }

  ngOnInit() {
    this.issueService.getMagazineIssuesEditor().subscribe(
      data => {
        console.log(data);
        this.issues = data;
      },
      erros => {
        console.log(erros);
      }
    );
  }

}
