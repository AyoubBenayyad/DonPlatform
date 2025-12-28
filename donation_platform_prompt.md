# Prompt Professionnel - Plateforme de Dons avec Spring Boot 3

## Contexte du Projet
Je dois développer une plateforme web complète de dons d'objets entre particuliers dans le cadre d'un projet universitaire. Le projet doit démontrer une maîtrise approfondie des concepts d'architecture REST, de scalabilité, et des bonnes pratiques de développement Spring Boot.

## Stack Technique Imposée
- **Framework**: Spring Boot 3.x
- **Base de données**: H2 (embarquée en dev, configurable pour production)
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven
- **Sécurité**: JWT avec roles (USER, ADMIN)
- **Documentation API**: OpenAPI/Swagger
- **Template Engine**: Thymeleaf (interface minimaliste mais futuriste)
- **Containerisation**: Docker (app + service SMTP)
- **Langue**: Français (code, commentaires, documentation)

## Spécifications Fonctionnelles Détaillées

### 1. Gestion des Annonces
**Entité Annonce doit inclure:**
- Titre (String, non vide)
- Description détaillée (Text)
- État de l'objet (Enum: NEUF, TRES_BON_ETAT, BON_ETAT, USAGE_ACCEPTABLE)
- Date de publication (auto-générée)
- Zone géographique (String: ville, code postal, région)
- Mode de remise (Enum: MAIN_PROPRE, ENVOI_POSSIBLE, LES_DEUX)
- Liste de mots-clés (Collection)
- Relation avec l'utilisateur créateur
- Statut (DISPONIBLE, RESERVE, DONNE)

**Fonctionnalités:**
- CRUD complet sur les annonces (seulement le créateur ou admin peut modifier/supprimer)
- Pagination des résultats
- Upload d'images (optionnel mais valorisé)

### 2. Système de Recherche et Filtrage
**Critères de recherche combinables:**
- Mots-clés (recherche full-text dans titre/description)
- Zone géographique (contient/égal)
- État de l'objet
- Mode de remise
- Date de publication (plage de dates)

**Recherches sauvegardées:**
- Uniquement pour utilisateurs authentifiés
- Possibilité d'activer/désactiver les notifications
- Stockage des critères de recherche sous forme JSON ou objet sérialisé

### 3. Système de Notifications
**Deux types:**
- **Notifications in-app**: Stockées en base, consultables via API
- **Notifications email**: Envoyées via SMTP pour nouvelles annonces matchant recherches sauvegardées

**Architecture:**
- Service de notification asynchrone (Scheduler Spring)
- Template d'email professionnel
- Configuration SMTP via Docker (MailHog ou service similaire)

### 4. Gestion des Favoris
- Relation Many-to-Many entre User et Annonce
- Endpoint pour ajouter/retirer des favoris
- Liste des favoris paginée

### 5. Création de Lots
- Possibilité de regrouper plusieurs annonces d'un même donneur
- Entité "Lot" avec liste d'annonces
- Validation: toutes les annonces doivent appartenir au même utilisateur

### 6. Messagerie Interne
**Système de conversation:**
- Entité Message (expediteur, destinataire, contenu, date, lu/non-lu)
- Endpoint pour envoyer un message
- Récupération des conversations (groupées par utilisateur)
- Marquage comme lu

## Architecture Technique Détaillée

### Structure du Projet (Architecture en couches)
```
src/main/java/com/plateforme/dons/
├── config/
│   ├── SecurityConfig.java (JWT, CORS, filters)
│   ├── OpenAPIConfig.java
│   └── AsyncConfig.java (pour notifications)
├── controller/
│   ├── AnnonceController.java
│   ├── AuthController.java (login, register)
│   ├── RechercheController.java
│   ├── FavoriController.java
│   ├── LotController.java
│   ├── MessageController.java
│   └── NotificationController.java
├── dto/ (Data Transfer Objects)
│   ├── request/
│   └── response/
├── entity/
│   ├── User.java
│   ├── Annonce.java
│   ├── RechercheSauvegardee.java
│   ├── Lot.java
│   ├── Message.java
│   └── Notification.java
├── repository/
├── service/
│   ├── impl/
│   └── interfaces
├── security/
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── UserDetailsServiceImpl.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── Custom exceptions
└── util/
```

### Modèle de Données Relationnel

**User**
- id (Long, PK)
- username (String, unique)
- email (String, unique)
- password (String, BCrypt)
- role (Enum: USER, ADMIN)
- dateInscription (LocalDateTime)
- Relations: OneToMany avec Annonce, ManyToMany avec Annonce (favoris)

**Annonce**
- id (Long, PK)
- titre, description, etat, datePublication, zone, modeRemise, statut
- createur (ManyToOne vers User)
- motsCles (ElementCollection)
- favorisParUsers (ManyToMany)

**RechercheSauvegardee**
- id, user, criteres (JSON/Text), notificationsActives, dateCreation

**Lot**
- id, titre, description, createur, annonces (ManyToMany)

**Message**
- id, expediteur, destinataire, contenu, dateEnvoi, lu

**Notification**
- id, user, type, contenu, dateCreation, lue

### Schema d'URL RESTful

#### Annonces
```
GET    /api/annonces                    - Liste paginée
GET    /api/annonces/{id}               - Détail
POST   /api/annonces                    - Création (AUTH)
PUT    /api/annonces/{id}               - Modification (AUTH, owner/admin)
DELETE /api/annonces/{id}               - Suppression (AUTH, owner/admin)
GET    /api/annonces/recherche          - Recherche avec filtres
```

#### Authentification
```
POST   /api/auth/register               - Inscription
POST   /api/auth/login                  - Connexion (retourne JWT)
GET    /api/auth/profile                - Profil user (AUTH)
```

#### Favoris
```
GET    /api/favoris                     - Mes favoris (AUTH)
POST   /api/favoris/{annonceId}         - Ajouter (AUTH)
DELETE /api/favoris/{annonceId}         - Retirer (AUTH)
```

#### Recherches Sauvegardées
```
GET    /api/recherches                  - Mes recherches (AUTH)
POST   /api/recherches                  - Sauvegarder (AUTH)
PUT    /api/recherches/{id}/notifications - Toggle notifs (AUTH)
DELETE /api/recherches/{id}             - Supprimer (AUTH)
```

#### Lots
```
GET    /api/lots                        - Liste
POST   /api/lots                        - Créer (AUTH)
GET    /api/lots/{id}                   - Détail
```

#### Messagerie
```
GET    /api/messages                    - Mes conversations (AUTH)
GET    /api/messages/{userId}           - Conversation avec user (AUTH)
POST   /api/messages                    - Envoyer message (AUTH)
PUT    /api/messages/{id}/lire          - Marquer comme lu (AUTH)
```

#### Notifications
```
GET    /api/notifications               - Mes notifications (AUTH)
PUT    /api/notifications/{id}/lire     - Marquer lue (AUTH)
```

### Négociation de Contenu
- **Accept: application/json** → Réponses JSON (API)
- **Accept: text/html** → Pages Thymeleaf (web interface)
- Utiliser `@RestController` avec `produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_HTML_VALUE}`

### Gestion du Cache et Optimisation
- **ETag** sur les ressources GET (annonces, profils)
- **Last-Modified** headers
- **Cache-Control** appropriés (public pour ressources statiques, private pour user-specific)
- Conditional requests (If-None-Match, If-Modified-Since)
- Pagination obligatoire sur toutes les listes

### Sécurité JWT
- Token valide 24h
- Refresh token (optionnel mais valorisé)
- Endpoints publics: /api/auth/*, /api/annonces (GET), /swagger-ui/*, /h2-console
- Tous les autres endpoints requièrent authentification

## Configuration Docker

### docker-compose.yml
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
    depends_on:
      - mailhog
    
  mailhog:
    image: mailhog/mailhog
    ports:
      - "1025:1025"  # SMTP
      - "8025:8025"  # Web UI
```

## Tests à Implémenter

### Tests Unitaires (JUnit 5 + Mockito)
- Services (logique métier)
- Repositories (requêtes personnalisées)
- DTOs validation

### Tests d'Intégration
- Controllers avec MockMvc
- Tests de sécurité (accès autorisé/refusé)
- Tests de négociation de contenu

### Tests Fonctionnels
- Scénarios complets end-to-end
- Utiliser @SpringBootTest avec TestRestTemplate

## Interface Web Thymeleaf (Minimaliste Futuriste)

### Design System
- **Palette**: Tons sombres (dark mode par défaut) avec accents néon (cyan, magenta)
- **Typography**: Police moderne sans-serif (Inter, Space Grotesk)
- **Layout**: Grid CSS moderne, espaces généreux
- **Components**: Cards glassmorphism, boutons avec hover effects
- **Animations**: Transitions subtiles CSS

### Pages principales
1. `/` - Accueil avec dernières annonces
2. `/annonces` - Liste avec filtres
3. `/annonces/{id}` - Détail
4. `/mon-compte` - Dashboard user
5. `/login` & `/register`
6. `/messages` - Messagerie

## Documentation à Fournir

### README.md
```markdown
# Plateforme de Dons - Projet Web Serveur

## Membres du Groupe
- [Nom Prénom 1]
- [Nom Prénom 2]

## Architecture
[Diagramme architecture en couches]

## Modèle de Données
[Schéma entités-relations]

## Lancement du Projet

### Avec Docker
docker-compose up --build

### Sans Docker
mvn clean install
mvn spring-boot:run

## Tests
mvn test

## Accès
- Application: http://localhost:8080
- API Docs: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
- MailHog: http://localhost:8025
```

### Document PDF (architecture-plateforme-dons.pdf)
**Sections obligatoires:**
1. **Introduction** - Contexte et objectifs
2. **Architecture globale** - Diagramme des couches, flux de données
3. **Modèle de données** - Schéma relationnel commenté
4. **API RESTful** - Pour chaque ressource:
   - URL schema
   - Méthodes HTTP supportées
   - Formats de représentation (JSON schemas)
   - Codes de réponse HTTP
   - Exemples de requêtes/réponses
5. **Gestion du cache** - Stratégie de cache, headers utilisés
6. **Sécurité** - Implémentation JWT, gestion des rôles
7. **Scalabilité** - Considérations pour passage à l'échelle
8. **Démarche suivie** - Méthodologie, difficultés rencontrées, solutions

## Critères de Qualité Attendus

### Code
- ✅ Nommage en français (variables, méthodes, classes)
- ✅ Commentaires Javadoc sur classes et méthodes publiques
- ✅ Gestion d'erreurs robuste (GlobalExceptionHandler)
- ✅ Validation des entrées (@Valid, custom validators)
- ✅ Logs appropriés (SLF4J)
- ✅ Respect principes SOLID
- ✅ Pas de duplication de code

### Architecture REST
- ✅ Utilisation correcte des verbes HTTP
- ✅ Codes de statut HTTP appropriés
- ✅ HATEOAS (liens hypermedia dans réponses)
- ✅ Versionning API (optionnel: /api/v1/)
- ✅ Pagination standardisée
- ✅ Filtrage/tri via query params

### Performance
- ✅ Lazy loading JPA configuré
- ✅ N+1 queries évitées (fetch joins)
- ✅ Indexes sur colonnes fréquemment requêtées
- ✅ Connection pool configuré

### Sécurité
- ✅ Mots de passe hashés (BCrypt)
- ✅ Protection CSRF désactivée (API REST)
- ✅ CORS configuré proprement
- ✅ Validation des JWT
- ✅ Pas de secrets en dur (properties externalisées)

## Points Bonus (pour impressionner)
- 🌟 Implémentation de webhooks pour notifications
- 🌟 Rate limiting sur endpoints sensibles
- 🌟 Metrics avec Actuator
- 🌟 Circuit breaker pattern (Resilience4j)
- 🌟 Internationalisation (i18n)
- 🌟 Export CSV/PDF des annonces
- 🌟 GraphQL endpoint alternatif
- 🌟 Tests de charge (JMeter, Gatling)

## Pièges à Éviter
- ❌ Retourner les entités JPA directement (utiliser DTOs)
- ❌ Oublier @Transactional sur méthodes de service
- ❌ Ignorer les cas limites (pagination dernière page vide, etc.)
- ❌ Ne pas gérer les time zones (utiliser UTC)
- ❌ Hardcoder des valeurs (ports, URLs, etc.)
- ❌ Négliger les messages d'erreur (toujours informatifs)

## Livrables Finaux
```
plateforme-dons.zip
├── src/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
├── architecture-plateforme-dons.pdf
└── .gitignore
```

---

## Approche de Développement Recommandée

### Phase 1: Setup & Base (Semaine 1-2)
1. Initialiser projet Spring Boot 3 avec dependencies
2. Configuration H2, JPA, sécurité basique
3. Entités principales + repositories
4. Authentification JWT fonctionnelle

### Phase 2: Core Features (Semaine 3-4)
5. CRUD Annonces complet avec tests
6. Système de recherche et filtrage
7. Gestion favoris
8. API documentation Swagger

### Phase 3: Features Avancées (Semaine 5-6)
9. Recherches sauvegardées
10. Système de notifications (in-app + email)
11. Messagerie interne
12. Gestion lots

### Phase 4: Polish & Déploiement (Semaine 7-8)
13. Interface Thymeleaf complète
14. Docker configuration
15. Tests complets (couverture >70%)
16. Documentation PDF finale
17. Optimisations performance

---

**Note importante**: Ce projet doit démontrer une compréhension approfondie des concepts REST, pas juste une application CRUD basique. Mettez l'accent sur la qualité de l'architecture, la gestion du cache, la scalabilité, et la négociation de contenu pour obtenir une excellente note.
