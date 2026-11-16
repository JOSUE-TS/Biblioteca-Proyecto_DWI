import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ApiService } from '../../../servicios/api.service';
import { Prestamo } from '../../../modelos/modelos';

@Component({
  selector: 'app-mis-prestamos',
  template: `
    <section class="page">
      <header><div><span class="eyebrow">MI ACTIVIDAD</span><h1>Mis préstamos</h1>
        <p>Consulta los libros que tienes y cuándo debes devolverlos.</p></div>
        <a routerLink="/catalogo">Explorar catálogo →</a></header>
      @if (error) { <p class="error">{{ error }}</p> }
      @if (cargando) { <p class="empty">Cargando tus préstamos…</p> }
      @else if (!prestamos.length) { <div class="empty"><h2>Aún no tienes préstamos</h2>
        <p>Abre la ficha de un libro disponible en el catálogo para solicitarlo.</p></div> }
      @else { <div class="loans">@for (p of prestamos; track p.id) {
        <article><div class="book"><span class="state" [class.returned]="p.fechaDevolucion">{{ p.fechaDevolucion ? 'DEVUELTO' : (vencido(p) ? 'VENCIDO' : 'EN CURSO') }}</span>
          <h2>{{ p.libro.titulo }}</h2><p>{{ p.libro.autor }}</p></div>
          <dl><div><dt>Retirado</dt><dd>{{ p.fechaPrestamo }}</dd></div><div><dt>Devolver antes de</dt><dd>{{ p.fechaEstimada }}</dd></div>
            @if (p.fechaDevolucion) { <div><dt>Devuelto el</dt><dd>{{ p.fechaDevolucion }}</dd></div> }
          </dl></article>
      }</div> }
    </section>
  `,
  styles: [`
    :host{display:block}.page{max-width:1100px;margin:auto;padding:clamp(30px,5vw,64px)}header{display:flex;justify-content:space-between;align-items:end;border-bottom:1px solid #bdb5a5;padding-bottom:25px;margin-bottom:28px}.eyebrow{font:600 10px 'DM Sans',sans-serif;letter-spacing:.16em;color:#a94730}h1{font:500 clamp(36px,5vw,56px)/1.1 'Shippori Mincho',Georgia,serif;margin:9px 0;color:#202723;letter-spacing:-.04em}header p,.book p{color:#686c66;font:13px 'DM Sans',sans-serif;margin:0}a{color:#a33620;text-decoration:none;font:600 12px 'DM Sans',sans-serif}.loans{display:grid;gap:12px}article{display:flex;justify-content:space-between;align-items:center;gap:24px;padding:23px 25px;background:#fff;border:1px solid #ded8ca}.state{font:600 9px 'DM Sans',sans-serif;letter-spacing:.1em;color:#4d6848}.state.returned{color:#777}h2{font:500 22px 'Shippori Mincho',Georgia,serif;color:#202723;margin:8px 0 4px}dl{display:flex;gap:32px;margin:0}dl div{min-width:120px}dt{font:10px 'DM Sans',sans-serif;color:#777}dd{font:12px 'DM Sans',sans-serif;color:#202723;margin:6px 0 0}.empty{padding:52px 20px;text-align:center;background:#fff;border:1px dashed #d8d1c3;color:#686c66}.empty h2{margin-top:0}.error{background:#f6e7e2;padding:12px;color:#873d2c}@media(max-width:650px){header,article{align-items:flex-start;flex-direction:column}dl{flex-wrap:wrap;gap:16px}}
  `],
  imports: [RouterLink],
})
export class MisPrestamosComponent implements OnInit {
  private readonly api = inject(ApiService);
  private readonly cdr = inject(ChangeDetectorRef);
  prestamos: Prestamo[] = [];
  cargando = true;
  error = '';
  ngOnInit(): void {
    this.api.misPrestamos().subscribe({
      next: (data) => { this.prestamos = data.sort((a, b) => b.id - a.id); this.cargando = false; this.cdr.markForCheck(); },
      error: () => { this.error = 'No se pudieron cargar tus préstamos.'; this.cargando = false; this.cdr.markForCheck(); },
    });
  }
  vencido(p: Prestamo): boolean { return !p.fechaDevolucion && p.fechaEstimada < new Date().toISOString().slice(0, 10); }
}
