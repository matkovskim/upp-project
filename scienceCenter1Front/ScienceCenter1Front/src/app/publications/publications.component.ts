import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MagazineService } from '../services/magazineService';

@Component({
  selector: 'app-publications',
  templateUrl: './publications.component.html',
  styleUrls: ['./publications.component.css']
})
export class PublicationsComponent implements OnInit {

  private magazineId:any;
  private publications = [];

  constructor(private activatedRoute: ActivatedRoute, private magazineService:MagazineService) { }

  ngOnInit() {
    
    this.activatedRoute.paramMap.subscribe(
      params => {
        this.magazineId = params.get('magazineId');
      });
      let x = this.magazineService.getAllMagazinesPublications(this.magazineId);

      x.subscribe(
        res => {
          this.publications=res;
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );


  }

}
