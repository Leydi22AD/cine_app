import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { UserHomeComponent } from './pages/user-home/user-home.component';
import { FuncionesPeliculaComponent } from './pages/funciones-pelicula/funciones-pelicula.component';
import { SeleccionAsientosComponent } from './pages/seleccion-asientos/seleccion-asientos.component';
import { PanelControlComponent } from './pages/panel-control/panel-control.component';
import { MisTicketsComponent } from './pages/mis-tickets/mis-tickets.component';
import { adminGuard, authGuard } from './guards/auth.guard';

// importa los nuevos componentes
import { FormFuncionComponent } from './pages/form-funcion/form-funcion.component';
import { FormPeliculaComponent } from './pages/form-pelicula/form-pelicula.component';
import { FormSalaComponent } from './pages/form-sala/form-sala.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'user/home',
    component: UserHomeComponent,
    canActivate: [authGuard]
  },
  {
    path: 'user/funciones/:id',
    component: FuncionesPeliculaComponent,
    canActivate: [authGuard]
  },
  {
    path: 'user/asientos/:id',
    component: SeleccionAsientosComponent,
    canActivate: [authGuard]
  },
  {
    path: 'user/mis-tickets',
    component: MisTicketsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'panel-control',
    component: PanelControlComponent,
    canActivate: [adminGuard]
  },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [adminGuard],
    children: [
      { path: 'home', component: HomeComponent },

      // Rutas de gestión para ADMIN
      { path: 'registrar-funcion', component: FormFuncionComponent },
      { path: 'registrar-pelicula', component: FormPeliculaComponent },
      { path: 'registrar-sala', component: FormSalaComponent },

      { path: '', redirectTo: 'home', pathMatch: 'full' },
    ],
  },
  { path: '**', redirectTo: 'login' }
];
