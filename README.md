# Basic Authentication API - Spring Boot (sans Spring Security ni JWT)

Une API d'authentification développée avec Spring Boot 3, offrant un système complet d'inscription, de connexion, de vérification d'email et de gestion des rôles, sans utiliser Spring Security ni JWT. Les sessions sont gérées via des tokens stockés côté base de données.

## Sommaire

- [Présentation générale](#présentation-générale)
- [Fonctionnalités](#fonctionnalités)
- [Architecture et conception](#architecture-et-conception)
- [Stack technique](#stack-technique)
- [Prérequis](#prérequis)
- [Installation et exécution](#installation-et-exécution)
- [Configuration (application.properties)](#configuration-srcmainresourcesapplicationproperties)
- [Endpoints de l'API (avec exemples)](#endpoints-de-lapi)
- [Modèles de données (DTOs)](#modèles-de-données-dtos)
- [Gestion des erreurs](#gestion-des-erreurs)
- [Journalisation (logs)](#journalisation-logs)
- [Développement & Tests](#développement--tests)
- [FAQ & Roadmap](#faq)

## Présentation générale

Ce projet illustre une approche d'authentification basique sans Spring Security/JWT, adaptée aux besoins de démonstration, POC ou services internes. Les sessions utilisateurs sont matérialisées par un en-tête HTTP `X-Session-Token` qui correspond à un token stocké en base pour chaque utilisateur connecté, avec une date d'expiration.

Attention: pour un système de production, envisagez l'utilisation de Spring Security, du stockage sécurisé des mots de passe (hashing), et de mécanismes de rafraîchissement de token.

## Fonctionnalités

- Gestion des utilisateurs: inscription, consultation, mise à jour, suppression
- Authentification: connexion/déconnexion avec token de session et expiration
- Vérification d'email: validation par token dédié à l'email
- Gestion des rôles: création, consultation, suppression (USER, ADMIN)
- Validation des données: contraintes robustes sur les DTOs
- Journalisation: logs applicatifs détaillés
- Gestion des erreurs: exceptions personnalisées centralisées

## Architecture et conception

- Couche Web (Controllers): expose des endpoints REST sous `/api/*`
- Couche Service: logique métier (authentification, rôles, utilisateurs, vérification email)
- Couche Repository: accès aux données via Spring Data JPA
- Modèles: entités `User` et `Role`
- Sessions: champs `sessionToken`, `sessionExpiry`, `isConnected` gérés sur l'entité `User`

Flux principaux:
- Inscription: `/api/register` → création d'utilisateur non vérifié, envoi/gestion d'un token de vérification
- Vérification email: `/api/register/verify-email` → active `emailVerified`
- Connexion: `/api/auth/login` → génère `sessionToken` + `sessionExpiry`
- Appels protégés: en-tête `X-Session-Token` requis
- Déconnexion: `/api/auth/logout` → invalide le token en base

## Stack technique

- Java 25
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8+
- Lombok
- Maven

## Prérequis

- Java 25
- Maven 3.6+
- MySQL en local (port 3306)

## Installation et exécution

1) Cloner le projet
```bash
git clone https://github.com/votre-organisation/authentication.git
cd authentication
```

2) Configurer la base MySQL (si nécessaire, ajustez l'utilisateur/mot de passe)

3) Lancer l'application
```bash
mvn spring-boot:run
```

Au démarrage, un jeu de données est initialisé:
- Rôles: ADMIN, USER
- Compte admin: username `admin`, email `adminRole@digipro.com`, mot de passe `secret` (email vérifié, session active par défaut pour démo)

## Configuration (src/main/resources/application.properties)

Paramètres par défaut:
- spring.datasource.url=jdbc:mysql://localhost:3306/auth_db?createDatabaseIfNotExist=true
- spring.datasource.username=root
- spring.datasource.password=
- spring.jpa.hibernate.ddl-auto=create-drop (réinitialise le schéma à chaque redémarrage en dev)
- logging.file.name=logs/authentication.log

Adaptez ces valeurs pour votre environnement.

## Endpoints de l'API

En-tête commun pour les endpoints protégés:
- `X-Session-Token: <token>`

1) Authentification (/api/auth)
- POST `/api/auth/login`
  - Body:
    - usernameOrEmail (string)
    - password (string)
  - Réponse 200:
    - sessionToken, expiry, userInfo { id, fullName, email, username, role }
- POST `/api/auth/logout`
  - Headers: X-Session-Token
  - Réponse 200 (vide)
- GET `/api/auth/me`
  - Headers: X-Session-Token
  - Réponse 200: userInfo { id, fullName, email, username, role }

2) Inscription & Vérification (/api/register)
- POST `/api/register`
  - Body: voir RegisterRequestDto ci-dessous
  - Réponse 200: RegisterResponseDto
- POST `/api/register/verify-email`
  - Body: VerifyEmailRequestDto (email, token)
  - Réponse 200: "Email verified successfully" ou 400 si échec

3) Utilisateurs (/api/users) [Protégé]
- GET `/api/users`
  - Liste tous les utilisateurs (200) ou 204 si vide
- GET `/api/users/{id}`
  - Détail utilisateur
- GET `/api/users/search?lastName=...`
  - Recherche par nom de famille
- PUT `/api/users/{id}`
  - Body: UpdateUserRequestDto
  - Met à jour l'utilisateur
- DELETE `/api/users/{id}`
  - Supprime l'utilisateur

4) Rôles (/api/roles) [Protégé]
- GET `/api/roles`
  - Liste des rôles
- GET `/api/roles/{id}`
  - Détail d'un rôle
- POST `/api/roles`
  - Body: RoleRequestDto
  - Crée un rôle (201)
- DELETE `/api/roles/{id}`
  - Supprime un rôle (204)

### Exemples curl

- Connexion
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"admin","password":"secret"}'
```

- Appel protégé (profil)
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "X-Session-Token: <VOTRE_TOKEN>"
```

- Inscription
```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName":"John",
    "lastName":"Doe",
    "email":"john.doe@example.com",
    "username":"johndoe",
    "password":"pass1234",
    "address":"1 rue Exemple",
    "postalCode":"75001",
    "city":"Paris",
    "country":"France"
  }'
```

## Modèles de données (DTOs)

- LoginRequestDto
  - usernameOrEmail: string
  - password: string

- LoginResponseDto
  - sessionToken: string
  - expiry: datetime
  - userInfo: { id: long, fullName: string, email: string, username: string, role: string }

- RegisterRequestDto (validations incluses)
  - firstName: lettres uniquement, requis
  - lastName: lettres uniquement, requis
  - email: format valide, requis
  - username: requis
  - password: requis
  - address: requis
  - postalCode: 5 chiffres, requis
  - city: lettres uniquement, requis
  - country: lettres uniquement, requis

- VerifyEmailRequestDto
  - email: string
  - token: string

- UpdateUserRequestDto
  - champs éditables du profil (adresse, ville, etc.)

- RoleRequestDto/RoleResponseDto
  - name: ADMIN | USER

## Gestion des erreurs

Exceptions personnalisées (converties par GlobalExceptionHandler):
- BadRequestException → 400 (ex: mot de passe invalide, email non vérifié)
- InvalidTokenException → 401/403 selon le contexte (token invalide/expiré)
- NoSuchUserException / NoSuchRoleException → 404
- RoleNameAlreadyInUseException / UsernameAlreadyInUseException → 409
- AlreadyConnectedException → 409 (session déjà active)

Le corps de réponse inclut un message d'erreur explicite. Référez-vous aux logs pour davantage de détails.

## Journalisation (logs)

- Fichier: `logs/authentication.log` (rotation configurée par le système)
- Niveaux: root=INFO, package app=DEBUG
- Traçabilité des actions: login, logout, vérification token, accès aux ressources

## Sécurité et bonnes pratiques

- Les mots de passe ne sont pas hashés dans ce POC: ne pas utiliser en production tel quel.
- Préférez Spring Security + BCrypt/Argon2 pour le hashing et des tokens JWT/OAuth2 pour la fédération.
- Utilisez HTTPS pour protéger `X-Session-Token` en transit.

## Développement & Tests

- Lancer l'appli: `mvn spring-boot:run`
- Tests unitaires: `mvn test`
- Build: `mvn clean package`

## FAQ

1) Je reçois "Invalid or expired session" → Vérifiez le header `X-Session-Token` et l'expiration.
2) Je ne vois aucun utilisateur → La base est régénérée à chaque démarrage (ddl-auto=create-drop).
3) Comment créer un rôle ? → POST `/api/roles` avec un token d'un utilisateur ADMIN.

## Roadmap (suggestions)

- Hashing des mots de passe (BCrypt)
- Migration vers Spring Security
- Ajout de refresh tokens et rotation
- Envoi d'emails réels pour la vérification (SMTP)
- Pagination/tri pour les listes

**Si ce projet vous a été utile, n'hésitez pas à lui donner une étoile ⭐**

Made with ❤️ by [Chéridanh TSIELA]
