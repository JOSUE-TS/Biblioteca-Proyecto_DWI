# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

- Administrators manage the book collection and loan records.
- Readers create an account, sign in, and browse the catalog.

## Product Purpose

Biblioteca is an academic library system for browsing and maintaining a real catalog and its loan workflow. Success means users can complete these tasks in the Angular front end while records and access rules remain authoritative in the Spring Boot API and its database.

## Operating Context

This is an integrated web development course project. The front end runs locally on Angular and consumes the separately running Spring Boot REST API.

## Capabilities and Constraints

- Angular front end with login, reader registration, book catalog, book create/edit, and administrator loan/devolution workflows.
- Spring Boot REST API owns persistence, authentication, authorization, and business rules.
- JWT authentication distinguishes the `ADMIN` and `LECTOR` roles.
- The interface must render API data and must not fabricate books, categories, or loans when the database is empty.
- Existing front-end routes and API paths must continue to work.

## Brand Commitments

- Product name: Biblioteca.
- Requested design personality: editorial bookstore.

## Evidence on Hand

- Existing Angular application: `front-Biblioteca`.
- Existing Spring Boot API project: `biblioteca`.
- Existing API models and endpoints for users, books, categories, loans, JWT login, and reader registration.
- No supplied logo, book photography, or catalog data should be invented.

## Product Principles

- Keep stored data authoritative in the back end.
- Make catalog and loan operations clear for both administrators and readers.
- Protect administrator tasks by role and preserve the API's security rules.
- Explain empty, loading, success, and error states so users know what to do next.
