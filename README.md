# Streaming Platform Project

A personal streaming platform inspired by Netflix, built with **React** and **Spring Boot**. This project is for learning and demonstration purposes — no actual movies are included for privacy reasons. Users can browse TV shows and movies, view details, and interact with content, while different user roles manage the platform with specific permissions.

---

## Features

### Public Features (No Authentication Required)
- Browse the home page with featured TV shows and movies.
- View detailed information about a series or movie.
- Archive page to filter content by:
  - Language
  - Type (TV show or movie)
  - Genre

### User Features (Authentication Required)
- Comment on movies and series.
- Like or dislike content.
- Add content to favorites.
- Interact with comments:
  - Like/dislike comments
  - Reply to comments
- User dashboard:
  - **Favorites** tab: Remove content from favorites.
  - **Comments** tab: Remove personal comments.

### Admin Features
- Admin dashboard with:
  - Bar chart showing top content with two filters:
    - Top 1-5
    - TV show or movie
  - Table of all users with search and filter options
  - Create, disable, or re-enable users
  - Disabled users see a message: “Account deactivated. Please contact administration.”

### Content Manager Features
- Manage content imported from **The Movie Database (TMDb)**:
  - Browse all movies and TV shows in a paginated table.
  - Search and filter content.
  - Import movies/series by language directly from TMDb (no manual entry required)
  - Edit or delete imported movies and TV shows
  - All imported content includes high-quality data, cast, genres, and images from TMDb

---

## Technology Stack

- **Frontend:** React, React Router, Axios
- **Backend:** Spring Boot, Spring Security, OAuth2 (Google), JWT
- **Database:** MySQL
- **External APIs:** [The Movie Database (TMDb) (https://www.themoviedb.org)]
- **Authentication & Authorization:** OAuth2 login with Google, JWT-based role management

---

## User Roles

1. **User**
   - Browse, comment, like/dislike, manage favorites
   - Dashboard with tabs for comments and favorite content

2. **Admin**
   - View analytics, top content, and manage users
   - Enable/disable users

3. **Content Manager**
   - Import movies and TV shows from TMDb
   - Edit or delete content
   - Manage all content displayed on the platform

---

## Security

- OAuth2 integration with Google for login
- JWT-based session management
- Role-based access control:
  - `ROLE_USER`
  - `ROLE_ADMIN`
  - `ROLE_CONTENT_MANAGER`

