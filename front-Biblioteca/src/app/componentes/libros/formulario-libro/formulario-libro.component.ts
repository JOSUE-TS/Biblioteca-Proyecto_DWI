import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ApiService } from '../../../servicios/api.service';
import { Categoria } from '../../../modelos/modelos';
import { of, switchMap, tap } from 'rxjs';

@Component({
  selector: 'app-formulario-libro',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './formulario-libro.component.html',
  styleUrl: './formulario-libro.component.scss',
})
export class FormularioLibroComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private api = inject(ApiService);
  private readonly cdr = inject(ChangeDetectorRef);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  categorias: Categoria[] = [];
  id?: number;
  cargando = false;
  error = '';
  errorPortada = '';
  archivoPortada: File | null = null;
  vistaPreviaPortada: string | null = null;
  portadaActual: string | null = null;
  form = this.fb.nonNullable.group({
    titulo: ['', [Validators.required, Validators.maxLength(160)]],
    autor: ['', [Validators.required, Validators.maxLength(120)]],
    editorial: [''],
    isbn: [''],
    anioPublicacion: [''],
    ejemplaresTotales: [1, [Validators.required, Validators.min(1), Validators.max(9999)]],
    categoriaId: [''],
  });
  ngOnInit(): void {
    this.api.categorias().subscribe({
      next: (d) => {
        this.categorias = d;
        this.cdr.markForCheck();
      },
      error: () => {
        this.error = 'No se pudieron cargar las categorías.';
        this.cdr.markForCheck();
      },
    });
    const value = this.route.snapshot.paramMap.get('id');
    if (value) {
      this.id = Number(value);
      this.api.libros().subscribe({
        next: (books) => {
          const b = books.find((x) => x.id === this.id);
          if (b)
            this.form.patchValue({
              titulo: b.titulo,
              autor: b.autor,
              editorial: b.editorial || '',
              isbn: b.isbn || '',
              anioPublicacion: b.anioPublicacion?.toString() || '',
              ejemplaresTotales: b.ejemplaresTotales || 1,
              categoriaId: b.categoria?.id.toString() || '',
            });
          else this.error = 'El libro solicitado no existe.';
          this.portadaActual = b?.portadaUrl ?? null;
          this.cdr.markForCheck();
        },
        error: () => {
          this.error = 'No se pudo cargar el libro.';
          this.cdr.markForCheck();
        },
      });
    }
  }
  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando = true;
    const v = this.form.getRawValue();
    const libro = {
      titulo: v.titulo.trim(),
      autor: v.autor.trim(),
      editorial: v.editorial.trim() || undefined,
      isbn: v.isbn.trim() || undefined,
      anioPublicacion: v.anioPublicacion ? Number(v.anioPublicacion) : undefined,
      ejemplaresTotales: Number(v.ejemplaresTotales),
      categoria: v.categoriaId ? { id: Number(v.categoriaId) } : null,
    };
    let libroGuardado = false;
    this.api
      .guardarLibro(libro, this.id)
      .pipe(
        tap(() => (libroGuardado = true)),
        switchMap((guardado) =>
          this.archivoPortada
            ? this.api.subirPortada(guardado.id, this.archivoPortada)
            : of(guardado),
        ),
      )
      .subscribe({
        next: () => void this.router.navigateByUrl('/catalogo'),
        error: (e) => {
          this.cargando = false;
          this.error = libroGuardado
            ? 'El libro se guardó, pero no se pudo subir la portada. Inténtalo de nuevo editando el libro.'
            : e.status === 409
              ? 'Ese ISBN ya está registrado.'
              : 'No se pudo guardar. Revisa los datos e inténtalo de nuevo.';
          this.cdr.markForCheck();
        },
      });
  }

  seleccionarPortada(event: Event): void {
    const input = event.target as HTMLInputElement;
    const archivo = input.files?.[0];
    if (!archivo) return;
    if (!['image/jpeg', 'image/png', 'image/webp'].includes(archivo.type)) {
      this.errorPortada = 'Elige una imagen JPG, PNG o WEBP.';
      input.value = '';
      this.cdr.markForCheck();
      return;
    }
    if (archivo.size > 5 * 1024 * 1024) {
      this.errorPortada = 'La imagen no debe superar 5 MB.';
      input.value = '';
      this.cdr.markForCheck();
      return;
    }
    this.liberarVistaPrevia();
    this.archivoPortada = archivo;
    this.vistaPreviaPortada = URL.createObjectURL(archivo);
    this.errorPortada = '';
  }

  quitarSeleccionPortada(input: HTMLInputElement): void {
    this.liberarVistaPrevia();
    this.archivoPortada = null;
    input.value = '';
  }

  get fuentePortada(): string | null {
    return this.vistaPreviaPortada ?? this.api.urlPortada(this.portadaActual);
  }

  ngOnDestroy(): void {
    this.liberarVistaPrevia();
  }

  private liberarVistaPrevia(): void {
    if (this.vistaPreviaPortada?.startsWith('blob:')) URL.revokeObjectURL(this.vistaPreviaPortada);
    this.vistaPreviaPortada = null;
  }
}
