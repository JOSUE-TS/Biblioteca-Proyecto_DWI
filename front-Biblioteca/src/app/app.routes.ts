import { Routes } from '@angular/router';
import { guardAdmin, guardSesion } from './guards/guards';
import { LoginComponent } from './componentes/autenticacion/login/login.component';
import { CatalogoLibrosComponent } from './componentes/catalogo/catalogo-libros/catalogo-libros.component';
import { FormularioLibroComponent } from './componentes/libros/formulario-libro/formulario-libro.component';
import { GestionPrestamosComponent } from './componentes/prestamos/gestion-prestamos/gestion-prestamos.component';
import { RegistroComponent } from './componentes/autenticacion/registro/registro.component';
import { GestionCategoriasComponent } from './componentes/categorias/gestion-categorias/gestion-categorias.component';
import { MisPrestamosComponent } from './componentes/prestamos/mis-prestamos/mis-prestamos.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'catalogo', component: CatalogoLibrosComponent, canActivate: [guardSesion] },
  { path: 'mis-prestamos', component: MisPrestamosComponent, canActivate: [guardSesion] },
  {
    path: 'libros/nuevo',
    component: FormularioLibroComponent,
    canActivate: [guardSesion, guardAdmin],
  },
  {
    path: 'libros/editar/:id',
    component: FormularioLibroComponent,
    canActivate: [guardSesion, guardAdmin],
  },
  {
    path: 'prestamos',
    component: GestionPrestamosComponent,
    canActivate: [guardSesion, guardAdmin],
  },
  {
    path: 'categorias',
    component: GestionCategoriasComponent,
    canActivate: [guardSesion, guardAdmin],
  },
  { path: '', pathMatch: 'full', redirectTo: 'catalogo' },
  { path: '**', redirectTo: 'catalogo' },
];
