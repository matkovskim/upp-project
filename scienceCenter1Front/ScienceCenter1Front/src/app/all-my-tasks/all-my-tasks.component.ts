import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repositoryService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-all-my-tasks',
  templateUrl: './all-my-tasks.component.html',
  styleUrls: ['./all-my-tasks.component.css']
})
export class AllMyTasksComponent implements OnInit {

  private tasks = [];

  constructor(private repositoryService: RepositoryService, private router: Router) {

    let x = this.repositoryService.getAllMyTasks();

    x.subscribe(
      res => {
        console.log(res);
        this.tasks = res;
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  ngOnInit() {
  }

  complete(taskId){
    console.log(taskId);
    this.router.navigate(['/taskDetails/'+taskId]);
  }

}
