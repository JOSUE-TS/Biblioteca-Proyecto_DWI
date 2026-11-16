import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../servicios/api.service';
import { Categoria } from '../../../modelos/modelos';

@Component({
  selector: 'app-gestion-categorias',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './gestion-categorias.component.html',
  styleUrl: './gestion-categorias.component.scss',
})
export class GestionCategoriasComponent implements OnInit {
  private readonly api = inject(ApiService);
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);
  categorias: Categoria[] = [];
  editando?: number;
  cargando = true;
  guardando = false;
  error = '';
  mensaje = '';
  readonly form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(80)]],
    descripcion: ['', [Validators.maxLength(240)]],
  });

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.api.categorias().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudieron cargar las categorías.';
        this.cargando = false;
        this.cdr.markForCheck();
      },
    });
  }

  editar(categoria: Categoria): void {
    this.editando = categoria.id;
    this.mensaje = '';
    this.error = '';
    this.form.setValue({
      nombre: categoria.nombre,
      descripcion: categoria.descripcion ?? '',
    });
  }

  cancelarEdicion(): void {
    this.editando = undefined;
    this.form.reset();
    this.error = '';
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando = true;
    this.error = '';
    this.mensaje = '';
    const datos = this.form.getRawValue();
    this.api
      .guardarCategoria(
        { nombre: datos.nombre.trim(), descripcion: datos.descripcion.trim() || undefined },
        this.editando,
      )
      .subscribe({
        next: () => {
          this.guardando = false;
          this.mensaje = this.editando ? 'Categoría actualizada.' : 'Categoría creada.';
          this.cancelarEdicion();
          this.cargar();
          this.cdr.markForCheck();
        },
        error: (e) => {
          this.guardando = false;
          this.error =
            e.status === 409
              ? 'Ya existe una categoría con ese nombre.'
              : 'No se pudo guardar la categoría. Revisa los datos e inténtalo de nuevo.';
          this.cdr.markForCheck();
        },
      });
  }

  borrar(categoria: Categoria): void {
    if (!confirm(`¿Eliminar la categoría “${categoria.nombre}”?`)) return;
    this.error = '';
    this.mensaje = '';
    this.api.borrarCategoria(categoria.id).subscribe({
      next: () => {
        this.mensaje = 'Categoría eliminada.';
        if (this.editando === categoria.id) this.cancelarEdicion();
        this.cargar();
      },
      error: (e) => {
        this.error =
          e.status === 409
            ? 'No se puede eliminar: hay libros que usan esta categoría.'
            : 'No se pudo eliminar la categoría.';
        this.cdr.markForCheck();
      },
    });
  }
}
