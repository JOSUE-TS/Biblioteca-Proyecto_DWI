import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenState = signal<string | null>(this.read('biblioteca_token'));
  private readonly roleState = signal<string | null>(this.read('biblioteca_rol'));
  private readonly emailState = signal<string | null>(this.read('biblioteca_email'));
  readonly token = this.tokenState.asReadonly();
  readonly rol = this.roleState.asReadonly();
  readonly email = this.emailState.asReadonly();
  readonly autenticado = computed(() => !!this.tokenState());
  readonly esAdmin = computed(() => this.roleState() === 'ADMIN');
  login(datos: { email: string; clave: string }) {
    return this.http.post<{ token: string; rol: string }>('/api/auth/login', datos).pipe(
      tap((r) => {
        sessionStorage.setItem('biblioteca_token', r.token);
        sessionStorage.setItem('biblioteca_rol', r.rol);
        sessionStorage.setItem('biblioteca_email', datos.email);
        this.tokenState.set(r.token);
        this.roleState.set(r.rol);
        this.emailState.set(datos.email);
      }),
    );
  }
  registro(datos: {
    nombre: string;
    apellido: string;
    email: string;
    clave: string;
  }) {
    return this.http.post('/api/auth/registro', { ...datos, rol: 'LECTOR' });
  }
  logout(): void {
    for (const k of ['biblioteca_token', 'biblioteca_rol', 'biblioteca_email'])
      sessionStorage.removeItem(k);
    this.tokenState.set(null);
    this.roleState.set(null);
    this.emailState.set(null);
  }
  private read(k: string): string | null {
    return typeof sessionStorage === 'undefined' ? null : sessionStorage.getItem(k);
  }
}
