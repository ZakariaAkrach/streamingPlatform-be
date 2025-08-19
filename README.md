# Streaming Platform Project

A personal streaming platform inspired by Netflix, built with **React** and **Spring Boot**. This project is for learning and demonstration purposes — no actual movies are included for privacy reasons. Users can browse TV shows and movies, view details, and interact with content, while different user roles manage the platform with specific permissions.

<img width="1873" height="922" alt="Streaming platform project photo 1" src="https://github.com/user-attachments/assets/cdcc4213-fe8e-4b86-baa9-26b8c9886e3a" />
<img width="1861" height="918" alt="Streaming platform project photo 3" src="https://github.com/user-attachments/assets/5aec4372-07f7-4cec-a129-27ca5169593a" />
<img width="1777" height="925" alt="Streaming platform project photo 4" src="https://github.com/user-attachments/assets/61c04e66-a730-400f-99b6-36993e88f95d" />
<img width="1876" height="857" alt="Streaming platform project photo 5" src="https://github.com/user-attachments/assets/8fd4e50b-52c2-4d93-a28d-b7fc3d3b63d0" />
<img width="1845" height="900" alt="Streaming platform project photo 6" src="https://github.com/user-attachments/assets/e6c58a5a-d282-497f-b090-a291a2413cc0" />
<img width="1853" height="694" alt="Streaming platform project photo 7" src="https://github.com/user-attachments/assets/9fc98335-67f4-4652-ae69-4a89902011c6" />
<img width="1812" height="828" alt="Streaming platform project photo 8" src="https://github.com/user-attachments/assets/6951b8f9-4575-4c15-8b8d-23aa26b46cdc" />
<img width="1852" height="887" alt="Streaming platform project photo 9" src="https://github.com/user-attachments/assets/de431a21-d1f1-42a8-b17d-25226323a085" />
<img width="1881" height="891" alt="Streaming platform project photo 11" src="https://github.com/user-attachments/assets/26306d20-8411-4224-a993-b4f3262cbb41" />
<img width="1886" height="858" alt="Streaming platform project photo 12" src="https://github.com/user-attachments/assets/f17c5510-eea1-4687-bc19-a754d762a695" />


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

