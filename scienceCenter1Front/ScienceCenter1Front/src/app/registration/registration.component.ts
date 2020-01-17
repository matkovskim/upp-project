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
  private formFieldsDto = null; //podaci o jednom polju forme
  private formFields = []; //sva polja fomre
  private enumValues = []; //sadrzaj jedne enumeracije
  private processInstance = ""; //id procesa
  private labels = []; //labele kod polja koja su tipa enumeracija
  private names = []; //niz koji sadrzi is polja, sluzi da se postavi name atributi u formi
  private enumerations = []; //niz svih enumeracija
  private dropdownSettings: any; //podesavanja za multiselect
  private dropdownList = []; //niz koji sadrzi mape i sluzi za prikaz u multiselectu
  private enumList = []; //jedna vrednost u enumeraciji
  private val: String; //string koji se dobija nakon submita, vrednosti enumeracije razdvojene sa zarezom


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
            this.enumList = [];
            this.enumValues = Object.keys(field.type.values);
            this.labels.push(field.label);
            for (const value of this.enumValues) {
              this.enumList.push({ item_id: value, item_text: value });
            }
            this.dropdownList.push(this.enumList);
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
  }

  onSubmit(value, form) {

    let o = new Array();
    for (var property in value) {
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
        }
        o.push({ fieldId: property, fieldValue: this.val });
      }
      else {
        o.push({ fieldId: property, fieldValue: value[property] });
      }
      this.val = "";
    }
    console.log(o);

    let x = this.repositoryService.registerUser(o, this.formFieldsDto.taskId);
    x.subscribe(
      res => {

        let y = this.repositoryService.nextTasks(this.processInstance);

        y.subscribe(
          res => {
            //da resimo problem dupliranja enumeracije
            this.names = [];
            this.labels = [];
            this.enumerations = [];
            this.dropdownList = [];

            if (res == null) {
              this.router.navigate(['checkMail']);
            }
            else {
              this.dropdownList = [];
              this.formFieldsDto = res;
              this.formFields = res.formFields;
              this.processInstance = res.processInstanceId;
              this.formFields.forEach((field) => {
                if (field.type.name == 'enum') {
                  this.enumList = [];
                  this.enumValues = Object.keys(field.type.values);
                  this.labels.push(field.label);
                  for (const value of this.enumValues) {
                    this.enumList.push({ item_id: value, item_text: value });
                  }
                  this.dropdownList.push(this.enumList);
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
