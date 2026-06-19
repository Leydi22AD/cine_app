import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(private router: Router) {}

  navegarAPelicula() {
    this.router.navigate(['/registrar-pelicula']);
  }

  navegarAFuncion() {
    this.router.navigate(['/registrar-funcion']);
  }

  navegarASala() {
    this.router.navigate(['/registrar-sala']);
  }
}
