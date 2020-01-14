import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repositoryService';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { NgLocalization } from '@angular/common';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  private formFieldsDto = null;
  private formFields = [];
  private enumValues = [];
  private enumerationValues = [];
  private processInstance = "";
  private tasks = [];

  constructor(private repositoryService: RepositoryService, private router: Router) {

    //Get all fields for form
    let x = repositoryService.startProcess();
    x.subscribe(
      res => {

        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach((field) => {
          if (field.type.name == 'enum') {
            this.enumValues = Object.keys(field.type.values);
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

    let x = this.repositoryService.registerUser(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {

        let y = this.repositoryService.getTasks(this.processInstance);

        y.subscribe(
          res => {
            if(res.length>0){
              alert(res);
              this.formFieldsDto = res;
              this.formFields = res[0].formFields;
              this.formFields.forEach((field) => {
                console.log(field);
                if (field.type.name == 'enum') {
                  this.enumValues = Object.keys(field.type.values);
                }
              });
            }
            else{
              this.router.navigate(['checkMail']);
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
