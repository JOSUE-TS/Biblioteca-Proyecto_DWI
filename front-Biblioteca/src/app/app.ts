import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from './servicios/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  get breadcrumb(): string {
    if (this.router.url.startsWith('/prestamos')) return 'CIRCULACIÓN / PRÉSTAMOS';
    if (this.router.url.startsWith('/mis-prestamos')) return 'MI ACTIVIDAD / PRÉSTAMOS';
    if (this.router.url.startsWith('/categorias')) return 'COLECCIÓN / CATEGORÍAS';
    if (this.router.url.startsWith('/libros/editar')) return 'COLECCIÓN / EDICIÓN';
    if (this.router.url.startsWith('/libros/nuevo')) return 'COLECCIÓN / REGISTRO';
    return '01 / PORTADA';
  }
  get cabecera(): string {
    return this.router.url.startsWith('/prestamos') || this.router.url.startsWith('/mis-prestamos')
      ? 'SISTEMA DE CIRCULACIÓN'
      : 'CATÁLOGO DE COLECCIÓN';
  }
  salir(): void {
    this.auth.logout();
    void this.router.navigateByUrl('/login');
  }
}
