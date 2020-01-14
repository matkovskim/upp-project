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
  private enumValues = [];
  private formFields = [];
  private dropdownList = [];
  private dropdownSettings: any;
  private val: String;
  private selectedItems = [];
  private selectedOneEnumItems = [];
  private formFieldsDto = null;
  private labels = [];
  private names = [];
  private enumerations = [];
  private enumList=[];

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private repositoryService: RepositoryService) { }

  ngOnInit() {
    this.val = "";
    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };

    this.activatedRoute.paramMap.subscribe(
      params => {
        this.taskId = params.get('taskId');
      });

    let x = this.repositoryService.getTaskInfo(this.taskId);

    x.subscribe(
      res => {
        this.task = res
        this.formFields = res.formFields;
        this.selectedItems=[];
        this.formFields.forEach((field) => {
          if (field.type.name == 'enum') {
            this.enumList = [];
            this.selectedOneEnumItems=[];
            this.enumValues = Object.keys(field.type.values);
            this.labels.push(field.label);
            for (const value of this.enumValues) {
              this.enumList.push({ item_id: value, item_text: value });
            }
            this.dropdownList.push(this.enumList);
            this.enumerations.push(this.enumValues);
            this.names.push(field.id);
            if(field.defaultValue!=null){
              this.selectedOneEnumItems.push({ item_id: field.defaultValue, item_text: field.defaultValue })
            }
            this.selectedItems.push(this.selectedOneEnumItems);
          }
          console.log(this.selectedItems);
          console.log(this.dropdownList);
        });
      },
      err => {
        alert(err.error);
      }
    );

  }

  onSubmit(value) {
    let o = new Array();
    for (let property in value) {
      if (typeof (value[property]) == "object") {
        for (var index = 0; index < value[property].length; index++) {
          if (index != 0) {
            this.val = this.val + ",";
            var something = value[property][index][Object.keys(value[property][index])[1]];
            this.val = this.val + something;
          }
          var something = value[property][index][Object.keys(value[property][index])[1]];
          this.val = this.val + something;
        }
        for (var fv in value[property]) {
          console.log(fv);
        }
        o.push({ fieldId: property, fieldValue: this.val });
      }
      else {
        o.push({ fieldId: property, fieldValue: value[property] });
      }
    }

    console.log(o);
    let x = this.repositoryService.completeTask(this.taskId, o);

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
