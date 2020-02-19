import { Component, OnInit } from '@angular/core';
import { ScientificPaperService } from '../services/scientific-paper.service';
import { ActivatedRoute } from '@angular/router';
import { IssueService } from '../services/issue.service';

@Component({
  selector: 'app-all-scientific-papers',
  templateUrl: './all-scientific-papers.component.html',
  styleUrls: ['./all-scientific-papers.component.css']
})
export class AllScientificPapersComponent implements OnInit {

  allPapers = [];
  issueId: number;

  constructor(private activatedRoute: ActivatedRoute, private scientificPaperService: ScientificPaperService,
    private issueService: IssueService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      this.issueId = +params.get('issueId');

      this.scientificPaperService.getAllPapersByIssue(this.issueId).subscribe(
        data => {
          console.log(data);
          this.allPapers = data;
        },
        errors => {
          console.log(errors);
        }
      );
    });
  }

}
