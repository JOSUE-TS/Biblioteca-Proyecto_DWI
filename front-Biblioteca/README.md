# Frontend de Biblioteca

Aplicación SPA desarrollada con Angular 21, TypeScript y SCSS. Consume la API REST del backend Spring Boot.

## Inicio local

1. Inicia MySQL con la base `biblioteca` y ejecuta el backend Spring Boot en `http://localhost:8080`.
2. En esta carpeta ejecuta `npm install` y luego `npm start`.
3. Abre `http://localhost:4200`.

El proxy de desarrollo (`proxy.conf.json`) redirige `/api/*` a `http://localhost:8080/*`, evitando configurar CORS para desarrollo. Para otro puerto, modifica `target` en ese archivo. En una instalación de producción, configura el servidor web para enrutar `/api` al backend.

## Roles

- El endpoint de registro crea lectores (`LECTOR`); el formulario no permite asignar el rol administrador.
- Los administradores pueden registrar y modificar libros, gestionar préstamos y registrar devoluciones.
- Los lectores pueden iniciar sesión y consultar el catálogo.
- La cuenta inicial de administrador se configura con `biblioteca.admin.email` y `biblioteca.admin.clave` en el backend.

## Funcionalidades

- Login, registro de lector, almacenamiento de sesión y envío automático de JWT con interceptor.
- Protección de páginas por sesión y rol.
- Catálogo con búsqueda, filtros y estado de disponibilidad.
- Formularios validados para crear y editar libros.
- Gestión de préstamos con selector de libros disponibles y lectores, fecha estimada, devolución y manejo de errores.
- Componentes standalone, rutas Angular y estilos SCSS responsivos.

## Organización del código

```text
src/app/
├── componentes/
│   ├── autenticacion/
│   │   ├── login/
│   │   └── registro/
│   ├── catalogo/catalogo-libros/
│   ├── libros/formulario-libro/
│   └── prestamos/gestion-prestamos/
├── guards/
├── interceptores/
├── modelos/
├── servicios/
├── app.routes.ts
└── app.config.ts
```

Cada componente reúne su lógica TypeScript, plantilla HTML y estilos SCSS. `app.routes.ts` conecta las pantallas; los servicios encapsulan HTTP y autenticación; los guards e interceptor aplican las reglas de navegación y el token.

## Compilación

```bash
npm run build
```
