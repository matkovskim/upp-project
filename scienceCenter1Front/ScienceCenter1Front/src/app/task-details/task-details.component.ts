import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RepositoryService } from '../services/repositoryService';

@Component({
  selector: 'app-task-details',
  templateUrl: './task-details.component.html',
  styleUrls: ['./task-details.component.css']
})
export class TaskDetailsComponent implements OnInit {

  private taskId: string;
  private task: any;
  private enumValues = []; //sadrzaj jedne enumeracije
  private formFields = []; //sva polja fomre
  private dropdownList = []; //niz koji sadrzi mape i sluzi za prikaz u multiselectu
  private dropdownSettings: any; //podesavanja za select
  private dropdownMultiselectSettings : any; //podesavanja za multiselect
  private multiselect=[]; //niz koji sadrzi informaciju je enumeracija multiselect
  private val: String; //string koji se dobija nakon submita, vrednosti enumeracije razdvojene sa zarezom
  private selectedItems = []; //niz koji sadrzi za svaki comboBox sta je selektovani
  private selectedOneEnumItems = []; //niz mala selektovanih polja u enumeraciji
  private formFieldsDto = null; //podaci o jednom polju forme
  private labels = []; //labele kod polja koja su tipa enumeracija
  private names = []; //niz koji sadrzi is polja, sluzi da se postavi name atributi u formi
  private enumerations = []; //niz svih enumeracija
  private enumList = []; //jedna vrednost u enumeraciji
  private readonlyList=[]; //lista booleana koji govore da li je readonly

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private repositoryService: RepositoryService) { }

  ngOnInit() {
    this.val = "";

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

    this.activatedRoute.paramMap.subscribe(
      params => {
        this.taskId = params.get('taskId');
      });

    let x = this.repositoryService.getTaskInfo(this.taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.readonlyList=[];
        this.task = res
        this.formFields = res.formFields;
        this.selectedItems=[];
        this.formFields.forEach((field) => {
          console.log(field.properties["readonly"]=="false");
          if(field.properties["readonly"]=="false"){
            this.readonlyList.push(false);
          }
          else{
            this.readonlyList.push(true);
          }
          if (field.type.name == 'enum') {
            this.enumList = [];
            this.selectedOneEnumItems=[];
            this.enumValues = Object.keys(field.type.values);
            this.labels.push(field.label);
            console.log("LABLES");
            console.log(field);
            for (const value of this.enumValues) {
              this.enumList.push({ item_id: value, item_text: value });
            }
            this.multiselect.push(field.properties[Object.keys(field.properties)[1]]);
            this.dropdownList.push(this.enumList);
            this.enumerations.push(this.enumValues);
            this.names.push(field.id);
            if(field.defaultValue!=null){
              this.selectedOneEnumItems.push({ item_id: field.defaultValue, item_text: field.defaultValue })
            }
            this.selectedItems.push(this.selectedOneEnumItems);
            console.log(this.selectedOneEnumItems);
          }
          console.log(this.readonlyList);
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
          else{
            var something = value[property][index][Object.keys(value[property][index])[1]];
            this.val = this.val + something;
          }
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
    let x = this.repositoryService.postData(o, this.taskId);

    x.subscribe(
      res => {
        this.router.navigate(['tasks']);
      },
      err => {
        alert(err.error);
      }
    );
  }

}
