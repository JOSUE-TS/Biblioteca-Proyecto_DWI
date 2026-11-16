---
name: Biblioteca
description: Catálogo académico con la voz visual de un sello editorial.
colors:
  imprint: "#a94730"
  imprint-deep: "#843925"
  paper: "#f8f8e8"
  index-paper: "#f4f1e8"
  ink: "#242a25"
  secondary-ink: "#686c66"
  rule: "#d8d1c3"
  active-paper: "#e9e3d6"
  cover-paper: "#d8cbb5"
typography:
  display:
    fontFamily: "Shippori Mincho, Georgia, serif"
    fontSize: "61px"
    fontWeight: 700
    lineHeight: 0.98
    letterSpacing: "-0.055em"
  heading:
    fontFamily: "Shippori Mincho, Georgia, serif"
    fontSize: "27px"
    fontWeight: 500
    lineHeight: 1.12
    letterSpacing: "-0.035em"
  body:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "12px"
    fontWeight: 400
    lineHeight: 1.7
    letterSpacing: "normal"
  label:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "9px"
    fontWeight: 500
    lineHeight: 1.4
    letterSpacing: "0.13em"
rounded:
  none: "0px"
  cover: "1px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "16px"
  lg: "24px"
  xl: "32px"
components:
  button-primary:
    backgroundColor: "{colors.imprint}"
    textColor: "#ffffff"
    rounded: "{rounded.none}"
    padding: "13px 17px"
  input:
    backgroundColor: "#fffdf7"
    textColor: "{colors.ink}"
    rounded: "{rounded.none}"
    height: "42px"
    padding: "0 11px"
---

# Design System: Biblioteca

## Overview

**Creative North Star: “El catálogo del sello editorial”**

Biblioteca presenta la colección como un catálogo de publicaciones: tipografía de imprenta, papel cálido, filetes finos y una marca terracota usada con moderación. El sistema conserva la familiaridad de una herramienta académica y deja que los registros de libros, lectores y préstamos sean el contenido principal.

La navegación funciona como un índice lateral. Las páginas de trabajo usan superficies planas y controles directos; los datos vienen de la API y los estados vacíos explican cómo comenzar sin rellenar la colección con ejemplos inventados.

**Key Characteristics:**

- Composición de catálogo editorial con índice fijo y área de lectura amplia.
- Shippori Mincho para títulos y DM Sans para controles y datos.
- Papel cálido, tinta oscura y acento terracota reservado para acciones.
- Catálogo y préstamos muestran solo datos persistidos por el backend.

## Colors

La paleta combina papel de lectura, un tono de índice ligeramente más oscuro y una tinta terracota inspirada en sellos de imprenta.

### Primary
- **Sello terracota** (`{colors.imprint}`): acciones principales, enlaces de alta prioridad y detalles impresos de la portada vacía.
- **Sello profundo** (`{colors.imprint-deep}`): estado hover de la acción primaria.

### Neutral
- **Papel de catálogo** (`{colors.paper}`): superficie principal de las páginas autenticadas.
- **Papel de índice** (`{colors.index-paper}`): barra lateral y fondo de registro.
- **Tinta** (`{colors.ink}`): títulos y texto de mayor prioridad.
- **Tinta secundaria** (`{colors.secondary-ink}`): descripciones y metadatos; mantener contraste legible.
- **Filete** (`{colors.rule}`): separadores y bordes de control.
- **Papel activo** (`{colors.active-paper}`): selección de navegación.
- **Papel de cubierta** (`{colors.cover-paper}`): ilustración tipográfica del estado de catálogo vacío.

**The Sello Rule.** El terracota marca acciones y estados; no se usa como decoración de paneles enteros.

## Typography

**Display Font:** Shippori Mincho (con Georgia como fallback)
**Body Font:** DM Sans (con Arial como fallback)

**Character:** Shippori Mincho aporta contraste de imprenta a los encabezados. DM Sans mantiene formularios, tablas y etiquetas compactos y claros.

### Hierarchy
- **Display** (700, 61px, line-height 0.98): título del catálogo en dos líneas.
- **Headline** (500, 54px, line-height 1.05): títulos de páginas de operación.
- **Title** (500, 27px, line-height 1.12): títulos de estados vacíos y bloques de formulario.
- **Body** (400, 12px, line-height 1.7): descripciones breves; el texto explicativo puede crecer hasta 65ch.
- **Label** (500, 9px, tracking 0.13em): estados, metadatos y cabeceras de tabla.

**The Two Voices Rule.** La serif vive en titulares y títulos de contenido; botones, datos, navegación y campos usan la sans.

## Layout

En escritorio, un índice lateral de 236px acompaña al área de trabajo; el contenido del catálogo puede extenderse hasta 1440px. Los títulos abren cada página, los contadores y herramientas se ordenan en bandas de una línea y el contenido comienza debajo de filetes finos. Bajo 760px el índice se reduce a 62px; las columnas de libros y formularios pasan a una sola columna en pantallas angostas.

## Elevation & Depth

La interfaz se mantiene plana. Los filetes, cambios de tono y el espacio separan regiones. La sombra desplazada aparece solo en la ilustración de cubierta del estado vacío, para sugerir páginas apiladas.

## Shapes

Los controles y las superficies de trabajo son rectangulares, con esquinas cuadradas. Los avatares mantienen su forma circular por convención. Los bordes son de 1px y usan el color de filete.

## Components

- **Botón principal:** fondo terracota, texto blanco, esquinas cuadradas; hover terracota profundo y foco visible.
- **Campo:** superficie casi blanca, borde fino, foco terracota y etiqueta explícita.
- **Gestor de categorías:** formulario breve y un índice de materias editable; la eliminación indica cuando hay libros asociados.
- **Índice lateral:** selección en papel activo; iconos SVG monolineales consistentes.
- **Portada de libro:** muestra la imagen elegida por administración; si no existe imagen, usa la marca tipográfica como respaldo.
- **Resumen de catálogo:** contadores tabulares en una banda delimitada por filetes, sin tarjetas métricas.
- **Estado vacío:** cubierta tipográfica y explicación junto a una acción para registrar el primer libro.
- **Tabla de préstamos:** cabeceras compactas, filas legibles y estado textual para cada movimiento.

## Do's and Don'ts

### Do
- Mantener la búsqueda, filtros y contadores vinculados a las respuestas de la API.
- Explicar los estados vacío, cargando y error con una acción de recuperación clara.
- Usar SVG para iconos de navegación y mantener los controles accesibles por teclado.
- Dejar que el lector filtre por categoría y disponibilidad usando los datos servidos por la API.

### Don't
- Inventar libros, categorías, lectores o préstamos para llenar la interfaz.
- Añadir tarjetas uniformes para cada grupo de información cuando un filete basta.
- Usar el acento terracota en adornos sin significado funcional.
- Añadir una línea de kicker o eyebrow encima de los títulos.
