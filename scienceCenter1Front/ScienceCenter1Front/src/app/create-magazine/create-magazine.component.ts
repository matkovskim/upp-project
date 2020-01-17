import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repositoryService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-magazine',
  templateUrl: './create-magazine.component.html',
  styleUrls: ['./create-magazine.component.css']
})
export class CreateMagazineComponent implements OnInit {

  private formFieldsDto = null; //podaci o jednom polju forme
  private formFields = []; //sva polja fomre
  private labels = []; //labele kod polja koja su tipa enumeracija
  private names = []; //niz koji sadrzi is polja, sluzi da se postavi name atributi u formi
  private enumValues = []; //sadrzaj jedne enumeracije
  private enumerations = []; //niz svih enumeracija
  private multiselect=[]; //niz boolean polja da li je enumeracija multiselect
  private processInstance = ""; //id procesa
  private dropdownSettings: any; //podesavanja za select
  private dropdownMultiselectSettings : any; //podesavanja za multiselect
  private dropdownList = []; //niz koji sadrzi mape i sluzi za prikaz u multiselectu
  private enumList = []; //jedna vrednost u enumeraciji
  private val: String; //string koji se dobija nakon submita, vrednosti enumeracije razdvojene sa zarezom

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
            this.enumList=[];
            this.enumValues = Object.keys(field.type.values);
            this.labels.push(field.label);
            for (const value of this.enumValues) {
              this.enumList.push({ item_id: value, item_text: value });
            }
            this.multiselect.push(field.properties[Object.keys(field.properties)[0]]);
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
    this.val="";
    this.dropdownMultiselectSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 3,
      allowSearchFilter: true
    };
    this.dropdownSettings = {
      singleSelection: true,
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
          else{
            var something = value[property][index][Object.keys(value[property][index])[1]];
            this.val = this.val + something;
          }
        }
        for (var fv in value[property]) {
        }
        o.push({ fieldId: property, fieldValue: this.val });
      }
      else {
        o.push({ fieldId: property, fieldValue: value[property] });
      }
      this.val="";
    }

    console.log(o);
    let x = this.repositoryService.postData(o, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        this.labels = [];
        this.names = [];
        this.enumValues = [];
        this.enumerations = [];
        this.multiselect=[];

        let y = this.repositoryService.getMyNextTask(this.processInstance);

        y.subscribe(
          res => {
            if(res==null){
              this.router.navigate(['']);
              alert("Časopis uspešno kreiran, čeka se odobravanje od strane urednika!");
            }
            else{
              console.log(res);
              this.dropdownList=[];
              this.formFieldsDto = res;
              this.formFields = res.formFields;
              this.processInstance = res.processInstanceId;
              this.formFields.forEach((field) => {
                if (field.type.name == 'enum') {
                  this.enumList=[];
                  this.enumValues = Object.keys(field.type.values);
                  this.labels.push(field.label);
                  for (const value of this.enumValues) {
                    this.enumList.push({ item_id: value, item_text: value });
                  }
                  this.dropdownList.push(this.enumList);
                  this.enumerations.push(this.enumValues);
                  this.names.push(field.id);
                  this.multiselect.push(field.properties[Object.keys(field.properties)[0]]);
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