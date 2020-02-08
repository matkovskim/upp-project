import { Component, OnInit} from '@angular/core';
import { ScientificAreaService } from '../services/scientificAreaService';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ConstantPool } from '@angular/compiler';
import { async } from '@angular/core/testing';
import { AuthentificationService } from '../services/authentification.service';

@Component({
  selector: 'app-create-editor',
  templateUrl: './create-editor.component.html',
  styleUrls: ['./create-editor.component.css']
})
export class CreateEditorComponent implements OnInit {

  private dropdownSettings: any; //podesavanja za multiselect
  private dropdownList = []; //niz koji sadrzi mape i sluzi za prikaz u multiselectu
  private registerEditorForm: FormGroup;
  private scientificAreas=[];
  private isLoaded=false;
  constructor(private scientificAreaService:ScientificAreaService, private authentificationService:AuthentificationService) {
   
    let x = this.scientificAreaService.getAllScientificAreas();
   
    x.subscribe(
      res => {
        this.scientificAreas=res;
        setTimeout(()=>{ 
          this.scientificAreas.forEach((scientificArea) => {
            this.dropdownList.push({ item_id: scientificArea.name, item_text: scientificArea.name });
          });
          this.isLoaded=true;
        }, 300);
      },
      err => {
        alert(err.error);
      }
    );    
   }

  ngOnInit() {
    this.registerEditorForm = new FormGroup(
      {
        username: new FormControl('', Validators.required),
        email: new FormControl('', Validators.required),
        name: new FormControl('', Validators.required),
        lastName: new FormControl('', Validators.required),
        city: new FormControl('', Validators.required),
        state: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required),
        title: new FormControl('', Validators.required),
        scientificAreas: new FormControl('', Validators.required)
      }
    );

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 5,
      allowSearchFilter: true
    };
  }

  submitForm() {
    let areas=this.registerEditorForm.value.scientificAreas;
    let areasString="";
    for (var index = 0; index < areas.length; index++) {
      if (index != 0) {
        areasString+=","
      }
       areasString+=areas[index]["item_id"];
    }

    const editorInfo: RegisterEditorModel = {
      username: this.registerEditorForm.value.username,
      email: this.registerEditorForm.value.email,
      name: this.registerEditorForm.value.name,
      lastName: this.registerEditorForm.value.lastName,
      city: this.registerEditorForm.value.city,
      state: this.registerEditorForm.value.state,
      password: this.registerEditorForm.value.password,
      title: this.registerEditorForm.value.title,
      scientificAreas: areasString
    };
    console.log(editorInfo);
    this.authentificationService.registerEditor(editorInfo).subscribe(
      data => {
        console.log(data);
        alert("USPESNO");
        window.location.href = '/adminPanel';
      },
      error => {
        alert("Došlo je do greške, pokušajte kasnije!");
      }
    );
    
  }
}

export interface RegisterEditorModel {
  username: string;
  email: string;
  name: string;
  lastName: string;
  password: string;
  city: string;
  state: string;
  title: string;
  scientificAreas: string;
}