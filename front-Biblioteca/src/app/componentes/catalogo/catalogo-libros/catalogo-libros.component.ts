import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../servicios/api.service';
import { AuthService } from '../../../servicios/auth.service';
import { Categoria, Libro } from '../../../modelos/modelos';

@Component({
  selector: 'app-catalogo-libros',
  imports: [RouterLink],
  templateUrl: './catalogo-libros.component.html',
  styleUrl: './catalogo-libros.component.scss',
})
export class CatalogoLibrosComponent implements OnInit {
  private api = inject(ApiService);
  private readonly cdr = inject(ChangeDetectorRef);
  readonly auth = inject(AuthService);
  libros: Libro[] = [];
  categorias: Categoria[] = [];
  filtrados: Libro[] = [];
  busqueda = '';
  filtro = 'todos';
  categoriaFiltro = 'todas';
  cargando = true;
  error = '';
  mensaje = '';
  seleccionado: Libro | null = null;
  fechaEstimada = this.fechaMasDias(14);
  solicitando = false;
  get disponibles(): number { return this.libros.reduce((s, l) => s + (l.ejemplaresDisponibles ?? (l.disponible ? 1 : 0)), 0); }
  get ocupados(): number { return this.libros.reduce((s, l) => s + (l.ejemplaresTotales ?? 1) - (l.ejemplaresDisponibles ?? (l.disponible ? 1 : 0)), 0); }
  ngOnInit(): void {
    this.cargar();
    this.api.categorias().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
        this.cdr.markForCheck();
      },
    });
  }
  cargar(): void {
    this.cargando = true;
    this.api.libros().subscribe({
      next: (d) => {
        this.libros = d;
        this.filtrar();
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudo cargar el catálogo. Verifica que el servidor esté activo.';
        this.cargando = false;
        this.cdr.markForCheck();
      },
    });
  }
  filtrar(): void {
    const q = this.busqueda.trim().toLocaleLowerCase();
    this.filtrados = this.libros.filter(
      (l) =>
        (this.filtro === 'todos' ||
          (this.filtro === 'disponibles' ? l.disponible : !l.disponible)) &&
        (this.categoriaFiltro === 'todas' ||
          String(l.categoria?.id ?? '') === this.categoriaFiltro) &&
        (!q ||
          `${l.titulo} ${l.autor} ${l.categoria?.nombre ?? ''}`.toLocaleLowerCase().includes(q)),
    );
  }
  borrar(libro: Libro): void {
    if (!confirm(`¿Eliminar “${libro.titulo}”?`)) return;
    this.api.borrarLibro(libro.id).subscribe({
      next: () => {
        this.mensaje = 'Libro eliminado.';
        this.cdr.markForCheck();
        this.cargar();
      },
      error: () => {
        this.error = 'No se pudo eliminar. Puede estar asociado a un préstamo.';
        this.cdr.markForCheck();
      },
    });
  }
  iniciales(titulo: string): string {
    return titulo
      .split(' ')
      .slice(0, 2)
      .map((s) => s[0])
      .join('')
      .toUpperCase();
  }

  urlPortada(portadaUrl?: string | null): string | null {
    return this.api.urlPortada(portadaUrl);
  }

  private fechaMasDias(dias: number): string {
    const fecha = new Date();
    fecha.setDate(fecha.getDate() + dias);
    fecha.setMinutes(fecha.getMinutes() - fecha.getTimezoneOffset());
    return fecha.toISOString().slice(0, 10);
  }

  solicitarPrestamo(): void {
    if (!this.seleccionado || !this.seleccionado.disponible || this.solicitando) return;
    this.solicitando = true;
    this.error = '';
    this.api.solicitarPrestamo({ libroId: this.seleccionado.id, fechaEstimada: this.fechaEstimada }).subscribe({
      next: () => {
        this.mensaje = `Préstamo de “${this.seleccionado?.titulo}” registrado.`;
        this.seleccionado = null;
        this.solicitando = false;
        this.cargar();
        this.cdr.markForCheck();
      },
      error: (e) => {
        this.error = e.status === 400 ? 'Este ejemplar ya no está disponible o la fecha no es válida.' : 'No se pudo solicitar el préstamo.';
        this.solicitando = false;
        this.cdr.markForCheck();
      },
    });
  }
}
