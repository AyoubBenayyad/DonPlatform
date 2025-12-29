# Architecture du Projet - Plateforme de Dons

**Membres du groupe :**
- **OUKESSO** Bakr
- **BENAYYAD** Ayoub

---

Ce document décrit l'architecture technique de la plateforme de dons, un projet web développé avec Spring Boot.

## Vue d'Ensemble

Le projet suit une architecture en couches classique MVC (Modèle-Vue-Contrôleur) avec une séparation claire des responsabilités. L'application permet aux utilisateurs de publier des annonces pour donner des objets et de rechercher des objets disponibles.

## Stack Technique

| Composant | Technologie |
|-----------|-------------|
| Framework | Spring Boot 3.3 |
| Sécurité | Spring Security + JWT |
| Persistance | Spring Data JPA + H2 |
| Templates | Thymeleaf |
| Stockage fichiers | MinIO |
| Email | JavaMail + MailHog |
| Conteneurisation | Docker |

---

## Ressources REST - Schémas d'URL et Méthodes

### 1. Ressource : Annonce

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| GET | `/api/annonces` | Liste toutes les annonces | Non |
| GET | `/api/annonces/{id}` | Détail d'une annonce | Non |
| POST | `/api/annonces` | Créer une annonce | Oui |
| PUT | `/api/annonces/{id}` | Modifier une annonce | Oui |
| PATCH | `/api/annonces/{id}/statut` | Changer le statut | Oui |
| DELETE | `/api/annonces/{id}` | Supprimer une annonce | Oui |

**Représentation JSON (création/modification) :**
```json
{
  "titre": "Chaise de bureau",
  "description": "Chaise ergonomique en bon état",
  "etat": "BON_ETAT",
  "zoneGeographique": "Paris",
  "modeRemise": "EN_MAIN_PROPRE",
  "motsCles": ["bureau", "mobilier"]
}
```

**Représentation JSON (réponse) :**
```json
{
  "id": 1,
  "titre": "Chaise de bureau",
  "description": "Chaise ergonomique en bon état",
  "etat": "BON_ETAT",
  "datePublication": "2024-12-29T10:30:00",
  "zoneGeographique": "Paris",
  "modeRemise": "EN_MAIN_PROPRE",
  "statut": "DISPONIBLE",
  "motsCles": ["bureau", "mobilier"],
  "images": ["http://minio:9000/images/img1.jpg"],
  "createur": {
    "id": 1,
    "username": "ayoub"
  }
}
```

---

### 2. Ressource : Utilisateur / Authentification

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| POST | `/api/auth/register` | Inscription | Non |
| POST | `/api/auth/login` | Connexion | Non |
| POST | `/api/auth/logout` | Déconnexion | Oui |
| GET | `/api/auth/me` | Info utilisateur courant | Oui |
| GET | `/api/auth/activate?token=xxx` | Activer le compte | Non |

**Représentation JSON (inscription) :**
```json
{
  "username": "ayoub",
  "email": "ayoub@example.com",
  "password": "motdepasse123"
}
```

**Représentation JSON (connexion) :**
```json
{
  "usernameOrEmail": "ayoub",
  "password": "motdepasse123"
}
```

**Représentation JSON (réponse connexion) :**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer"
}
```

---

### 3. Ressource : Favoris

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| GET | `/api/favorites/ids` | Liste des IDs des favoris | Oui |
| POST | `/api/favorites/toggle/{id}` | Ajouter/retirer un favori | Oui |

**Représentation JSON (GET /api/favorites/ids) :**
```json
[1, 5, 12]
```

---

### 4. Ressource : Notifications

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| GET | `/api/notifications` | Liste des notifications | Oui |
| GET | `/api/notifications/unread-count` | Nombre de non lues | Oui |
| POST | `/api/notifications/{id}/read` | Marquer comme lue | Oui |

**Représentation JSON (notification) :**
```json
{
  "id": 1,
  "message": "Nouvelle annonce correspondant à votre recherche",
  "dateCreation": "2024-12-29T14:00:00",
  "isRead": false
}
```

---

### 5. Ressource : Recherches Sauvegardées

| Méthode | URL | Description | Auth |
|---------|-----|-------------|------|
| GET | `/api/saved-searches` | Liste des recherches | Oui |
| POST | `/api/saved-searches` | Sauvegarder une recherche | Oui |
| DELETE | `/api/saved-searches/{id}` | Supprimer une recherche | Oui |

**Représentation JSON (création) :**
```json
{
  "motsCles": "chaise bureau",
  "zone": "Paris",
  "etat": "BON_ETAT"
}
```

---


## Architecture en Couches

```
┌─────────────────────────────────────────────────────────┐
│                    Présentation                         │
│  (Thymeleaf Templates, REST Controllers, Web Controllers)│
├─────────────────────────────────────────────────────────┤
│                     Service                             │
│        (Logique métier, Validation, Orchestration)      │
├─────────────────────────────────────────────────────────┤
│                    Repository                           │
│              (Accès aux données via JPA)                │
├─────────────────────────────────────────────────────────┤
│                     Entités                             │
│                (Modèle de données)                      │
└─────────────────────────────────────────────────────────┘
```

## Diagramme de Classes (Entités)

```
+------------------+       +-------------------+
|      User        |       |     Annonce       |
+------------------+       +-------------------+
| - id: Long       |       | - id: Long        |
| - username       |  1..* | - titre           |
| - email          |<------| - description     |
| - password       |       | - etat: EtatObjet |
| - role: Role     |       | - datePublication |
| - dateInscription|       | - zoneGeographique|
| - enabled        |       | - modeRemise      |
| - activationToken|       | - statut          |
+------------------+       | - motsCles: Set   |
        |                  | - images: Set     |
        | 1..*             +-------------------+
        v                          ^
+------------------+               | *..* (favoris)
|  Notification    |               |
+------------------+       +-------+
| - id: Long       |       |
| - message        |       |
| - dateCreation   |<------+ User.favoris
| - isRead         |
+------------------+
        ^
        | 1..*
+----------------------+
| RechercheSauvegarde  |
+----------------------+
| - id: Long           |
| - motsCles           |
| - zone               |
| - etat               |
| - dateCreation       |
+----------------------+

Enumérations:
- Role: USER, ADMIN
- EtatObjet: NEUF, BON_ETAT, USAGE
- StatutAnnonce: DISPONIBLE, RESERVE, DONNE
- ModeRemise: EN_MAIN_PROPRE, LIVRAISON
```

## Diagramme de Séquence - Ajout aux Favoris

```
Utilisateur      Frontend       Controller      Service        Repository       DB
    |               |               |              |               |            |
    |--Clic coeur-->|               |              |               |            |
    |               |--POST /api/favorites/toggle/{id}------------>|            |
    |               |               |--toggleFavorite()----------->|            |
    |               |               |              |--findByUsername()--------->|
    |               |               |              |<---------User--------------|
    |               |               |              |--add/remove favori         |
    |               |               |              |--save(user)--------------->|
    |               |               |              |<---------OK----------------|
    |               |               |<-------------void------------------------|
    |               |<--------------HTTP 200 OK----|              |            |
    |               |--Update UI--->|               |              |            |
    |               |               |               |              |            |
```

## Diagramme de Séquence - Authentification

```
Utilisateur        AuthController       AuthService      JwtProvider        DB
    |                   |                   |                |               |
    |--POST /login----->|                   |                |               |
    |                   |--authenticate()-->|                |               |
    |                   |                   |--verify------->|               |
    |                   |                   |<---User valid--|               |
    |                   |                   |--generateToken()-------------->|
    |                   |                   |<---JWT Token---|               |
    |                   |<--JwtResponse-----|                |               |
    |<--Token+Cookie----|                   |                |               |
    |                   |                   |                |               |
    |   [Requêtes suivantes avec Cookie JWT]                 |               |
    |--GET /api/xx----->|                   |                |               |
    |                   |--validateToken()----------------->|               |
    |                   |<-----------------Valid------------|               |
    |                   |--Traitement normal                 |               |
```

## Description des Packages

### `entity` - Modèle de Données
Contient les entités JPA qui représentent les tables de la base de données :
- **User** : Utilisateur de la plateforme (implémente UserDetails pour Spring Security)
- **Annonce** : Publication d'un objet à donner
- **Notification** : Message de notification pour l'utilisateur
- **RechercheSauvegarde** : Critères de recherche enregistrés

### `repository` - Couche d'Accès aux Données
Interfaces Spring Data JPA pour les opérations CRUD :
- `UserRepository` : Recherche par username, email, token d'activation
- `AnnonceRepository` : Filtrage par statut, zone, créateur
- `NotificationRepository` : Notifications par utilisateur et statut lu/non-lu

### `service` - Logique Métier
Services qui encapsulent la logique métier :
- **AnnonceService** : CRUD des annonces, gestion des images
- **FavoriteService** : Gestion des favoris utilisateur
- **EmailService** : Envoi d'emails (activation, notifications)
- **NotificationService** : Création et gestion des notifications

### `controller` - Contrôleurs
Deux types de contrôleurs :
- **REST Controllers** (`@RestController`) : API JSON pour le frontend JS
- **Web Controllers** (`@Controller`) : Rendu des pages Thymeleaf

### `security` - Sécurité
Configuration de Spring Security :
- **JwtTokenProvider** : Génération et validation des tokens JWT
- **JwtAuthenticationFilter** : Filtre pour extraire et valider le JWT
- **SecurityConfig** : Configuration des règles d'accès

### `config` - Configuration
Classes de configuration Spring :
- **OpenAPIConfig** : Documentation Swagger/OpenAPI
- **MinioConfig** : Configuration du stockage d'images

## Flux de Données

### Création d'une Annonce
1. L'utilisateur remplit le formulaire
2. Le frontend envoie les données + images à `AnnonceController`
3. Les images sont uploadées vers MinIO via `ImageService`
4. L'annonce est créée via `AnnonceService`
5. Un événement `AnnonceCreatedEvent` est publié
6. `AnnonceEventListener` vérifie les recherches sauvegardées
7. Des notifications sont créées pour les utilisateurs concernés

### Système de Notifications
Le système utilise les événements Spring :
1. Une action déclenche un événement (ex: nouvelle annonce)
2. Le listener capture l'événement
3. Il vérifie si des utilisateurs ont des recherches correspondantes
4. Des notifications sont créées et des emails envoyés

## Sécurité

### Authentification JWT
- Les tokens JWT sont stockés dans des cookies HttpOnly
- Expiration configurable (par défaut 24h)
- Chaque requête API vérifie le token via `JwtAuthenticationFilter`

### Protection des Endpoints
```
Publics :
- GET /api/annonces/**
- POST /api/auth/login, /api/auth/register
- Pages statiques

Authentifiés :
- POST/PUT/DELETE /api/annonces/**
- /api/favorites/**
- /api/notifications/**
- /favoris, /mon-profil
```

## Base de Données

### Schéma Simplifié

```
users
├── id (PK)
├── username (UNIQUE)
├── email (UNIQUE)
├── password
├── role
└── enabled

annonces
├── id (PK)
├── titre
├── description
├── user_id (FK -> users)
└── statut

user_favoris (table de jointure)
├── user_id (FK -> users)
└── annonce_id (FK -> annonces)

notifications
├── id (PK)
├── user_id (FK -> users)
├── message
└── is_read
```

## Déploiement Docker

Le fichier `docker-compose.yml` définit trois services :

1. **app** : L'application Spring Boot
2. **mailhog** : Serveur SMTP pour les emails de développement
3. **minio** : Stockage compatible S3 pour les images

Les données sont persistées via des volumes Docker.

## Points d'Amélioration Futurs

- Ajout de tests unitaires et d'intégration
- Migration vers PostgreSQL pour la production
- Mise en cache avec Redis
- Recherche full-text avec Elasticsearch
- WebSocket pour les notifications en temps réel
