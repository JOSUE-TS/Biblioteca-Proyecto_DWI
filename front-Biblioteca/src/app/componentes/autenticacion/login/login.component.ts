import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../servicios/auth.service';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private readonly cdr = inject(ChangeDetectorRef);
  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    clave: ['', [Validators.required, Validators.minLength(6)]],
  });
  cargando = false;
  error = '';
  readonly registrado = this.route.snapshot.queryParamMap.get('registrado') === '1';
  entrar(): void {
    this.error = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando = true;
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.cargando = false;
        void this.router.navigateByUrl(
          this.route.snapshot.queryParamMap.get('returnUrl') || '/catalogo',
        );
      },
      error: (e) => {
        this.cargando = false;
        this.error =
          e.status === 0
            ? 'No se pudo conectar con la biblioteca.'
            : 'Correo o contraseña incorrectos.';
        this.cdr.markForCheck();
      },
    });
  }
}
