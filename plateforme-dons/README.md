# Plateforme de Dons - Donation Platform

Une plateforme web permettant aux utilisateurs de donner ou récupérer des objets gratuitement.

## Prérequis

- Docker et Docker Compose
- (Optionnel) Java 17+ et Maven pour le développement local

## Lancement du Projet

### Avec Docker (Recommandé)

```bash
cd plateforme-dons
docker-compose up --build
```

L'application sera accessible sur : **http://localhost:8080**

### Services Additionnels

| Service | URL | Description |
|---------|-----|-------------|
| Application | http://localhost:8080 | Interface principale |
| MailHog | http://localhost:8025 | Visualisation des emails |
| MinIO Console | http://localhost:9001 | Stockage des images |
| H2 Console | http://localhost:8080/h2-console | Base de données |

## Comptes de Test

| Rôle | Username | Mot de passe |
|------|----------|--------------|
| Utilisateur | `ayoub` | `password123` |
| Utilisateur | `user` | `password123` |

## Fonctionnalités et Comment les Tester

### 1. Authentification

**Inscription :**
1. Aller sur `/register`
2. Remplir le formulaire (username, email, mot de passe)
3. Vérifier l'email d'activation dans MailHog (http://localhost:8025)
4. Cliquer sur le lien d'activation

**Connexion :**
1. Aller sur `/login`
2. Entrer les identifiants
3. Le token JWT est stocké automatiquement

### 2. Gestion des Annonces

**Créer une annonce :**
1. Se connecter
2. Cliquer sur "Donner un objet"
3. Remplir titre, description, état, zone géographique
4. Uploader des images (optionnel)
5. Valider

**Consulter les annonces :**
1. Aller sur `/annonces`
2. Utiliser les filtres (catégorie, zone, état)
3. Cliquer sur une annonce pour voir les détails

### 3. Système de Favoris

**Ajouter un favori :**
1. Se connecter
2. Sur la page `/annonces`, cliquer sur l'icône cœur
3. Le cœur devient rouge

**Voir ses favoris :**
1. Cliquer sur "Favoris" dans le menu
2. La page affiche toutes les annonces sauvegardées

### 4. Recherches Sauvegardées

**Sauvegarder une recherche :**
1. Effectuer une recherche avec des filtres
2. Cliquer sur "Sauvegarder cette recherche"
3. La recherche apparaît dans "Mes Recherches"

**Notifications automatiques :**
- Quand une nouvelle annonce correspond à une recherche sauvegardée, une notification est envoyée

### 5. Notifications

**Consulter les notifications in-app :**
1. L'icône cloche dans la navbar affiche le nombre de notifications non lues
2. Cliquer dessus pour voir la liste

---

## Tester les Emails avec MailHog

L'application utilise **MailHog** pour intercepter tous les emails en développement. Cela permet de tester les fonctionnalités d'envoi d'emails sans avoir besoin d'un vrai serveur SMTP.

**URL MailHog : http://localhost:8025**

### Emails à tester :

| Fonctionnalité | Comment tester | Où voir l'email |
|----------------|----------------|-----------------|
| **Activation de compte** | S'inscrire via `/register` | MailHog affiche l'email avec le lien d'activation |
| **Notifications de recherches** | Créer une recherche sauvegardée, puis créer une annonce correspondante | MailHog affiche la notification par email |

### Tester l'activation de compte :
1. Aller sur **http://localhost:8080/register**
2. Créer un compte avec un email quelconque (ex: test@example.com)
3. Ouvrir **http://localhost:8025** (MailHog)
4. L'email d'activation apparaît dans la boîte de réception
5. Cliquer sur le lien d'activation dans l'email
6. Le compte est maintenant actif et peut se connecter

## Structure des Dossiers

```
plateforme-dons/
├── src/main/java/com/plateforme/dons/
│   ├── controller/       # Contrôleurs REST et Web
│   ├── entity/           # Entités JPA
│   ├── repository/       # Repositories Spring Data
│   ├── service/          # Logique métier
│   ├── security/         # Configuration JWT
│   ├── dto/              # Objets de transfert
│   └── config/           # Configuration Spring
├── src/main/resources/
│   ├── templates/        # Templates Thymeleaf
│   └── static/           # CSS, JS, images
├── docker-compose.yml    # Configuration Docker
└── pom.xml               # Dépendances Maven
```

## API REST

### Authentification
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion
- `GET /api/auth/activate?token=...` - Activation du compte

### Annonces
- `GET /api/annonces` - Liste des annonces
- `GET /api/annonces/{id}` - Détail d'une annonce
- `POST /api/annonces` - Créer une annonce
- `PUT /api/annonces/{id}` - Modifier une annonce
- `DELETE /api/annonces/{id}` - Supprimer une annonce

### Favoris
- `GET /api/favorites/ids` - IDs des favoris de l'utilisateur
- `POST /api/favorites/toggle/{id}` - Ajouter/retirer un favori

### Notifications
- `GET /api/notifications` - Liste des notifications
- `GET /api/notifications/unread-count` - Nombre de non lues

## Technologies Utilisées

- **Backend:** Spring Boot 3.3, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, HTML/CSS, JavaScript
- **Base de données:** H2 (développement)
- **Stockage:** MinIO (images)
- **Email:** MailHog (développement)
- **Conteneurisation:** Docker

## Arrêter le Projet

```bash
docker-compose down
```

Pour supprimer aussi les données :
```bash
docker-compose down -v
```
