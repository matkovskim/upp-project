import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RepositoryService } from '../services/repositoryService';

@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.css']
})
export class TaskDetailsComponent implements OnInit {

  taskId: string;
  private task: any;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private repositoryService: RepositoryService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(

      params => {
        console.log(params);
        this.taskId = params.get('taskId');
        console.log(this.taskId);
      });

    let x = this.repositoryService.getTaskInfo(this.taskId);

    x.subscribe(
      res => {
        this.task = res
        console.log(this.task);
      },
      err => {
        alert(err.error);
      }
    );

  }

  onSubmit(value) {

    let o = new Array();
    for (var property in value) {
      o.push({ fieldId: property, fieldValue: value[property] });
    }

    console.log(o);
    let x = this.repositoryService.rewieverAcceptance(this.taskId, o);

    x.subscribe(
      res => {
        this.router.navigate(['']);
      },
      err => {
        alert(err.error);
      }
    );
  }

}
