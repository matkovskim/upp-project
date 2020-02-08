import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repositoryService';
import { Router } from '@angular/router';
import { __core_private_testing_placeholder__ } from '@angular/core/testing';

@Component({
  selector: 'app-processing-text',
  templateUrl: './processing-text.component.html',
  styleUrls: ['./processing-text.component.css']
})
export class ProcessingTextComponent implements OnInit {

  private formFieldsDto = null; //podaci o jednom polju forme
  private formFields = []; //sva polja fomre
  private labels = []; //labele kod polja koja su tipa enumeracija
  private stringLabels = []; //labele kod polja koja su tipa string
  private names = []; //niz koji sadrzi is polja, sluzi da se postavi name atributi u formi
  private enumValues = []; //sadrzaj jedne enumeracije
  private enumerations = []; //niz svih enumeracija
  private strings = []; //niz svih stringova
  private multiselect=[]; //niz boolean polja da li je enumeracija multiselect
  private processInstance = ""; //id procesa
  private dropdownSettings: any; //podesavanja za select
  private dropdownMultiselectSettings : any; //podesavanja za multiselect
  private dropdownList = []; //niz koji sadrzi mape i sluzi za prikaz u multiselectu
  private enumList = []; //jedna vrednost u enumeraciji
  private val: String; //string koji se dobija nakon submita, vrednosti enumeracije razdvojene sa zarezom
  private pdf=[];

  constructor(private repositoryService: RepositoryService, private router: Router) { 
    let x = repositoryService.startProcessingTextProcess();
    x.subscribe(
      res => {
        console.log(res.formFields);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach((field) => {
          if (field.type.name == 'string') {
            this.stringLabels.push(field.label);
            this.strings.push(field);
            if(field.properties[Object.keys(field.properties)[0]]=='true'){
              this.pdf.push('true')
            }
            else{
              this.pdf.push('false')
            }
          }
          if (field.type.name == 'enum') {
            this.enumList=[];
            this.enumValues = Object.keys(field.type.values);
            console.log(this.enumValues)
            this.labels.push(field.label);
            for (const value of this.enumValues) {
              this.enumList.push({ item_id: value, item_text: value });
              console.log(this.enumList);
            }
            if(field.properties[Object.keys(field.properties)[0]]==undefined){
              this.multiselect.push('false');
            }
            else{
              this.multiselect.push(field.properties[Object.keys(field.properties)[0]]);
            }
            this.dropdownList.push(this.enumList);
            console.log(this.dropdownList);
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
      console.log(property);
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
        this.stringLabels=[];
        this.strings=[];
        this.names = [];
        this.enumValues = [];
        this.enumerations = [];
        this.multiselect=[];
        this.pdf=[];

        let y = this.repositoryService.getMyNextTask(this.processInstance);

        y.subscribe(
          res => {
            if(res==null){
              this.router.navigate(['']);
             // alert("Časopis uspešno kreiran, čeka se odobravanje od strane administratora!");
            }
            else{
              console.log(res);
              this.dropdownList=[];
              this.formFieldsDto = res;
              this.formFields = res.formFields;
              this.processInstance = res.processInstanceId;
              this.formFields.forEach((field) => {
                if(field.type.name == 'string'){
                    this.stringLabels.push(field.label);
                    this.strings.push(field);
                    if(field.properties[Object.keys(field.properties)[1]]=='true'){
                      this.pdf.push('true')
                    }
                    else{
                      this.pdf.push('false')
                    }
                }
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
