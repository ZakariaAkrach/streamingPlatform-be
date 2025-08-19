# Streaming Platform Project

A personal streaming platform inspired by Netflix, built with **React** and **Spring Boot**. This project is for learning and demonstration purposes — no actual movies are included for privacy reasons. Users can browse TV shows and movies, view details, and interact with content, while different user roles manage the platform with specific permissions.

<img width="1873" height="922" alt="Streaming platform project photo 1" src="https://github.com/user-attachments/assets/cdcc4213-fe8e-4b86-baa9-26b8c9886e3a" />
<img width="1725" height="920" alt="Streaming platform project photo 2" src="https://github.com/user-attachments/assets/8b0f3e86-05e0-45f8-bedb-0c49240e3f59" />
<img width="1861" height="918" alt="Streaming platform project photo 3" src="https://github.com/user-attachments/assets/6417b004-9eb9-43d8-842a-8390d92e2197" />
<img width="1777" height="925" alt="Streaming platform project photo 4" src="https://github.com/user-attachments/assets/7375f357-a75e-4b09-9e34-ebb9752a0506" />
<img width="1876" height="857" alt="Streaming platform project photo 5" src="https://github.com/user-attachments/assets/02a9d605-739e-4c28-93bb-98177e9d273e" />
<img width="1845" height="900" alt="Streaming platform project photo 6" src="https://github.com/user-attachments/assets/573cb385-5d8a-4efc-a3bc-e36b8603f1e5" />
<img width="1853" height="902" alt="Streaming platform project photo 7" src="https://github.com/user-attachments/assets/a3efa006-259c-41e2-8605-6a39f8c76482" />
<img width="1812" height="828" alt="Streaming platform project photo 8" src="https://github.com/user-attachments/assets/b0ab05f1-2bcc-49ec-9f97-0b025559c42b" />
<img width="1852" height="887" alt="Streaming platform project photo 9" src="https://github.com/user-attachments/assets/754dabca-e53b-4021-adf7-389c43da3ca7" />
<img width="1857" height="897" alt="Streaming platform project photo 10" src="https://github.com/user-attachments/assets/2d76655d-27ae-43ba-9b0f-36d5e5cd56d5" />
<img width="1881" height="891" alt="Streaming platform project photo 11" src="https://github.com/user-attachments/assets/3f941614-e0b0-4753-a091-b316870cf79a" />

For the frontend part of the project, visit the repository [here](https://github.com/ZakariaAkrach/streamingPlatform-fe).

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

