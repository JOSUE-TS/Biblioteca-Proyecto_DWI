import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Categoria, Libro, Prestamo, Usuario } from '../modelos/modelos';
type LibroPayload = Omit<Partial<Libro>, 'categoria'> & { categoria?: { id: number } | null };

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly base = '/api';
  libros() {
    return this.http.get<Libro[]>(`${this.base}/libros`);
  }
  urlPortada(portadaUrl?: string | null): string | null {
    return portadaUrl ? `${this.base}${portadaUrl}` : null;
  }
  subirPortada(id: number, archivo: File) {
    const datos = new FormData();
    datos.append('archivo', archivo);
    return this.http.post<Libro>(`${this.base}/libros/${id}/portada`, datos);
  }
  quitarPortada(id: number) {
    return this.http.delete<Libro>(`${this.base}/libros/${id}/portada`);
  }
  guardarLibro(libro: LibroPayload, id?: number) {
    return id
      ? this.http.put<Libro>(`${this.base}/libros/${id}`, libro)
      : this.http.post<Libro>(`${this.base}/libros`, libro);
  }
  borrarLibro(id: number) {
    return this.http.delete<void>(`${this.base}/libros/${id}`);
  }
  categorias() {
    return this.http.get<Categoria[]>(`${this.base}/categorias`);
  }
  guardarCategoria(categoria: Pick<Categoria, 'nombre' | 'descripcion'>, id?: number) {
    return id
      ? this.http.put<Categoria>(`${this.base}/categorias/${id}`, categoria)
      : this.http.post<Categoria>(`${this.base}/categorias`, categoria);
  }
  borrarCategoria(id: number) {
    return this.http.delete<void>(`${this.base}/categorias/${id}`);
  }
  prestamos() {
    return this.http.get<Prestamo[]>(`${this.base}/prestamos`);
  }
  misPrestamos() {
    return this.http.get<Prestamo[]>(`${this.base}/prestamos/mios`);
  }
  solicitarPrestamo(data: { libroId: number; fechaEstimada: string; observacion?: string }) {
    return this.http.post<Prestamo>(`${this.base}/prestamos/solicitar`, data);
  }
  usuarios() {
    return this.http.get<Usuario[]>(`${this.base}/usuarios`);
  }
  crearPrestamo(data: {
    libroId: number;
    usuarioId: number;
    fechaEstimada: string;
    observacion: string;
  }) {
    return this.http.post<Prestamo>(`${this.base}/prestamos`, data);
  }
  devolver(id: number) {
    return this.http.put<Prestamo>(`${this.base}/prestamos/${id}/devolver`, {});
  }
}
