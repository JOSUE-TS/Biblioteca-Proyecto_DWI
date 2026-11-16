# Catalog and authenticated application surface

## Scope and visitor mode
The scope is the existing Angular application: sign-in and reader registration, catalog, book registration/editing, and administration of loans. Mode: Operate. The catalog is the primary surface; the same visual system extends to the other routes.

## Audience and task
Administrators maintain the collection and circulation records. Readers sign in and browse the real catalog. The primary catalog task is finding a title; for an empty collection, the administrator should see how to add the first book.

## Content and states
Books, categories, readers, and loans come from the Spring Boot API. The current catalog and category endpoints return no records. The interface must show a truthful empty state, then display real records after creation. Never add fake titles, borrowers, totals, or testimonials.

## Constraints
Preserve the Angular routes, API paths, HTTP behavior, JWT interceptor, role checks, validations, and existing loan rules. Keep the interface responsive and Spanish-language. No supplied logo or book-cover images exist.

## Chosen direction and memorable moment
Editorial bookstore, requested by the user. Catalog composition approved by the user: “Portada de colección.” The collection's first real entry becomes the opening book in the catalog; the empty state gives administrators a direct way to register it.

## Unresolved decisions
No product or data questions remain. Do not infer a nonempty catalog from mock content.

## Direction contract
THESIS: Turn the catalog into the cover and working index of a publisher's trade catalog, refusing generic dashboard tiles. Make each real book feel like a deliberate entry; make the empty collection an intentional opening page with a direct first-book action.

OWN-WORLD: Use the approved uncoated-stock palette of pale paper, deep ink, and a single imprint red. Build hierarchy through a large literary title, an asymmetrical cover field, rule-bound metadata, and a stamped primary action. Keep actual controls legible and familiar. Carry the cover/index motif into login, registration, book forms, and loans without repeating the same composition.

STORY: Readers see the catalog and its availability. Administrators can search, filter, create or edit books, and manage due dates and returns. When the API has no books, explain that state and lead to registration; all records and totals remain API-derived.

FIRST VIEWPORT: At 1440×900, keep a narrow catalog index rail at left and a spacious working page at right. Set “Catálogo de libros” large at upper left, with the registration action on the right. Put availability counts on one ruled line, search and filters below, then a compact typographic book-cover panel beside the empty-state explanation and first-book action. With records present, the lower field becomes the actual catalog grid.

FORM: Publisher's trade catalog, candidate 7 in the grounded list; seed 9aa1498f. The user selected the editorial bookstore identity and approved the cover-led composition.

FINISH: unreviewed and undocumented is unfinished; this build ends with the finish review, the verdict, DESIGN.md, and every shipping raster carrying its provenance
