import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IssueService } from '../services/issue.service';
import { MagazineService } from '../services/magazine.service';

@Component({
  selector: 'app-all-issues',
  templateUrl: './all-issues.component.html',
  styleUrls: ['./all-issues.component.css']
})
export class AllIssuesComponent implements OnInit {

  allIssues = [];
  magazineId: number;
  constructor(private activatedRoute: ActivatedRoute, private issueService: IssueService, private magazineService: MagazineService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      this.magazineId = +params.get('magazineId');
      this.issueService.getMagazineIssues(this.magazineId).subscribe(
        data => {
          console.log(data);
          this.allIssues = data;
        },
        errors => {
          console.log(errors);
        }
      );
    });
  }

}
