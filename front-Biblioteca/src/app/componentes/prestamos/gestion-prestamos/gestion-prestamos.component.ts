import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../../servicios/api.service';
import { Libro, Prestamo, Usuario } from '../../../modelos/modelos';

@Component({
  selector: 'app-gestion-prestamos',
  imports: [ReactiveFormsModule],
  templateUrl: './gestion-prestamos.component.html',
  styleUrl: './gestion-prestamos.component.scss',
})
export class GestionPrestamosComponent implements OnInit {
  private api = inject(ApiService);
  private fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);
  prestamos: Prestamo[] = [];
  usuarios: Usuario[] = [];
  libros: Libro[] = [];
  cargando = true;
  error = '';
  ok = '';
  modal = false;
  form = this.fb.nonNullable.group({
    libroId: ['', [Validators.required]],
    usuarioId: ['', [Validators.required]],
    fechaEstimada: ['', [Validators.required]],
    observacion: [''],
  });
  readonly hoy = (() => {
    const d = new Date();
    d.setMinutes(d.getMinutes() - d.getTimezoneOffset());
    return d.toISOString().slice(0, 10);
  })();
  ngOnInit(): void {
    this.cargar();
    this.api.usuarios().subscribe({
      next: (d) => {
        this.usuarios = d;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudieron cargar usuarios.';
        this.cdr.markForCheck();
      },
    });
    this.api.libros().subscribe({
      next: (d) => {
        this.libros = d.filter((l) => l.disponible);
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudieron cargar los libros disponibles.';
        this.cdr.markForCheck();
      },
    });
  }
  cargar(): void {
    this.cargando = true;
    this.api.prestamos().subscribe({
      next: (d) => {
        this.prestamos = d.sort((a, b) => b.id - a.id);
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudieron cargar los préstamos.';
        this.cargando = false;
        this.cdr.markForCheck();
      },
    });
  }
  get activos(): number {
    return this.prestamos.filter((p) => !p.fechaDevolucion).length;
  }
  registrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const v = this.form.getRawValue();
    this.api
      .crearPrestamo({
        libroId: Number(v.libroId),
        usuarioId: Number(v.usuarioId),
        fechaEstimada: v.fechaEstimada,
        observacion: v.observacion,
      })
      .subscribe({
        next: () => {
          this.modal = false;
          this.form.reset();
          this.ok = 'Préstamo registrado correctamente.';
          this.cdr.markForCheck();
          this.cargar();
        },
        error: (e) => {
          this.error =
            e.status === 400
              ? 'El libro no está disponible o la fecha no es válida.'
              : 'No se pudo registrar el préstamo.';
          this.cdr.markForCheck();
        },
      });
  }
  devolver(p: Prestamo): void {
    if (!confirm(`¿Registrar la devolución de “${p.libro.titulo}”?`)) return;
    this.api.devolver(p.id).subscribe({
      next: () => {
        this.ok = 'Devolución registrada.';
        this.cdr.markForCheck();
        this.cargar();
      },
      error: () => {
        this.error = 'No se pudo registrar la devolución.';
        this.cdr.markForCheck();
      },
    });
  }
}
