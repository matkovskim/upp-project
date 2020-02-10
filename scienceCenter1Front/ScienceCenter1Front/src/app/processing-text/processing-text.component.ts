import { Component, OnInit } from '@angular/core';
import { RepositoryService } from '../services/repositoryService';
import { Router } from '@angular/router';
import { __core_private_testing_placeholder__ } from '@angular/core/testing';
import { UploadService } from '../services/upload.service';

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
  private currentFileUpload: File;
  private fileName="";

  constructor(private repositoryService: RepositoryService, private router: Router, private uploadService:UploadService) { 
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
            if(field.properties[Object.keys(field.properties)[2]]=='true'){
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
            if(field.properties[Object.keys(field.properties)[0]]==undefined){
              this.multiselect.push('false');
            }
            else{
              this.multiselect.push(field.properties[Object.keys(field.properties)[0]]);
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

  selectFile(event) {
    this.currentFileUpload = event.target.files.item(0);
    this.fileName=this.currentFileUpload.name;
  }

  containsPDF(){
    for(let i=0 ;i<this.pdf.length;i++){
      if(this.pdf[i]=='true'){
        return true;
      }
    }
    return false;
  }

  onSubmit(value, form) {
    console.log(this.pdf);
    
    if(this.containsPDF()==true){
      console.log("true je");
      let x=this.uploadService.sendPDF(this.currentFileUpload, this.processInstance);
        x.subscribe(
          res => {
            console.log(res);
            console.log('File is completely uploaded!');
            this.getNext(value);
            this.pdf=[];
          },
          errors => {
            console.log(errors);
          }
      );
  
      this.currentFileUpload = undefined;
    }
    else{
      this.getNext(value);
    }

  }

getNext(value){
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
      if(res!=null && res.name!=null){
        window.location.href=res.name;
      }
      else{
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
            console.log(res);
            if(res==null){
              this.router.navigate(['']);
            }
            else{
              this.dropdownList=[];
              this.formFieldsDto = res;
              this.formFields = res.formFields;
              this.processInstance = res.processInstanceId;
              this.formFields.forEach((field) => {
                if(field.type.name == 'string'){
                    this.stringLabels.push(field.label);
                    this.strings.push(field);
                    if(field.properties[Object.keys(field.properties)[2]]=='true'){
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
      }
     
    },
    err => {
      alert(err.error);
    }
  );

}



}
