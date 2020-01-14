import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repositoryService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-magazine',
  templateUrl: './create-magazine.component.html',
  styleUrls: ['./create-magazine.component.css']
})
export class CreateMagazineComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private labels = [];
  private names = [];
  private enumValues = [];
  private enumerations = [];
  private processInstance = "";

  constructor(private repositoryService: RepositoryService, private router: Router) {

    //Start process and get all fields for form
    let x = repositoryService.startMagazineProcess();
    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach((field) => {
          if (field.type.name == 'enum') {
            this.enumValues = Object.keys(field.type.values);
            this.labels.push(field.label);
            this.enumerations.push(this.enumValues);
            this.names.push(field.id);
          }
        });
      },
      err => {
        alert(err.error);
      }
    );
  }

  ngOnInit() {
  }

  onSubmit(value, form) {

    let o = new Array();
    for (var property in value) {
      if (typeof (value[property]) == "object") {
        o.push({ fieldId: property, fieldValue: (value[property].toString()) });
      }
      else {
        o.push({ fieldId: property, fieldValue: value[property] });
      }
    }

    let x = this.repositoryService.createMagazine(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.labels = [];
        this.names = [];
        this.enumValues = [];
        this.enumerations = [];
      
        let y = this.repositoryService.nextTasks(this.processInstance);

        y.subscribe(
          res => {
            if(res==null){
              this.router.navigate(['']);
              alert("Časopis uspešno kreiran, čeka se odobravanje od strane urednika!");
            }
            else{
              this.formFieldsDto = res;
              this.formFields = res.formFields;
              this.processInstance = res.processInstanceId;
              this.formFields.forEach((field) => {
                if (field.type.name == 'enum') {
                  this.enumValues = Object.keys(field.type.values);
                  this.labels.push(field.label);
                  this.enumerations.push(this.enumValues);
                  this.names.push(field.id);
                }
              });
            }
           
            
          },
          err => {
            alert(err.error);
          }
        );
      },
      err => {
        alert(err.error);
      }
    );

  }

}