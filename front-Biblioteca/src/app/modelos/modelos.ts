export interface Categoria {
  id: number;
  nombre: string;
  descripcion?: string;
}
export interface Libro {
  id: number;
  titulo: string;
  autor: string;
  editorial?: string;
  isbn?: string;
  anioPublicacion?: number;
  disponible: boolean;
  ejemplaresTotales: number;
  ejemplaresDisponibles: number;
  portadaUrl?: string | null;
  categoria?: Categoria | null;
}
export interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  codigo: string;
  email: string;
  rol: 'ADMIN' | 'LECTOR';
}
export interface Prestamo {
  id: number;
  libro: Libro;
  usuario: Usuario;
  fechaPrestamo: string;
  fechaEstimada: string;
  fechaDevolucion?: string | null;
  observacion?: string;
}
