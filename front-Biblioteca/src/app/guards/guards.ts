import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../servicios/auth.service';
export const guardSesion: CanActivateFn = (_route, state) => {
  const auth = inject(AuthService);
  return (
    auth.autenticado() ||
    inject(Router).createUrlTree(['/login'], { queryParams: { returnUrl: state.url } })
  );
};
export const guardAdmin: CanActivateFn = () => {
  const auth = inject(AuthService);
  return auth.esAdmin() || inject(Router).createUrlTree(['/catalogo']);
};
