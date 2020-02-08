import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import { AuthentificationService } from '../services/authentification.service';

@Component({
  selector: 'app-create-admin',
  templateUrl: './create-admin.component.html',
  styleUrls: ['./create-admin.component.css']
})
export class CreateAdminComponent implements OnInit {

  private registerAdminForm: FormGroup;

  constructor(private authentificationService: AuthentificationService) { }

  ngOnInit() {
    this.registerAdminForm = new FormGroup(
      {
        username: new FormControl('', Validators.required),
        email: new FormControl('', Validators.required),
        name: new FormControl('', Validators.required),
        lastName: new FormControl('', Validators.required),
        city: new FormControl('', Validators.required),
        state: new FormControl('', Validators.required),
        password: new FormControl('', Validators.required)
      }
    );
  }

  submitForm() {
    const adminInfo: RegisterAdminModel = {
      username: this.registerAdminForm.value.username,
      email: this.registerAdminForm.value.email,
      name: this.registerAdminForm.value.name,
      lastName: this.registerAdminForm.value.lastName,
      city: this.registerAdminForm.value.city,
      state: this.registerAdminForm.value.state,
      password: this.registerAdminForm.value.password
    };
    console.log(adminInfo);
    this.authentificationService.registerAdmin(adminInfo).subscribe(
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

export interface RegisterAdminModel {
  username: string;
  email: string;
  name: string;
  lastName: string;
  password: string;
  city: string;
  state: string;
}