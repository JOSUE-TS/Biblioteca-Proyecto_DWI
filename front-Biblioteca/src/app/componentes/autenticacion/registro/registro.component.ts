import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../servicios/auth.service';
@Component({
  selector: 'app-registro',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.scss',
})
export class RegistroComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private readonly cdr = inject(ChangeDetectorRef);
  cargando = false;
  error = '';
  form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(80)]],
    apellido: ['', [Validators.required, Validators.maxLength(80)]],
    email: ['', [Validators.required, Validators.email]],
    clave: ['', [Validators.required, Validators.minLength(8)]],
  });
  registrar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando = true;
    this.auth.registro(this.form.getRawValue()).subscribe({
      next: () => void this.router.navigate(['/login'], { queryParams: { registrado: '1' } }),
      error: (e) => {
        this.cargando = false;
        this.error =
          e.status === 409
            ? 'El correo o código ya está registrado.'
            : 'No se pudo crear la cuenta. Revisa los datos e inténtalo otra vez.';
        this.cdr.markForCheck();
      },
    });
  }
}
