import { HttpInterceptorFn } from '@angular/common/http';
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token =
    typeof sessionStorage === 'undefined' ? null : sessionStorage.getItem('biblioteca_token');
  return next(
    token && req.url.startsWith('/api/') && !req.url.endsWith('/auth/login')
      ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : req,
  );
};
