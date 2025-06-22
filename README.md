# Streaming Platform
This is a streaming platform web application built with **React** (frontend) and **Spring Boot** (backend).
I started this project recently to learn full-stack development, deepen my knowledge of Spring Boot, and improve my React skills.

> This repository is part of a full-stack application. You can find the frontend [here](https://github.com/ZakariaAkrach/streamingPlatform-fe).

## Project Overview
The platform supports three roles:
- **User:**
Users can browse movies and TV shows from the home page, view detailed information, and if logged in watch videos, write comments, interact with others (e.g., reply, like, dislike), and add movies or shows to their favorites or mark them with a like/dislike.
They also have access to a personal dashboard where they can view their favorite content and manage their comment history.
- **Admin:**
Has access to dashboards with site analytics, including trends, most viewed movies, and more, with filtering options.
- **Content Manager:**
The Content Manager has a paginated table where they can filter and sort by title or other columns, edit and delete movies, 
and import movies or TV shows from an external API through a dynamic table that allows filtering by language and content type, 
with previews showing the title, image, and other details before importing.

# Features & Technologies
- I imported and stored in my database about 1100 movies and 1100 TV shows from The Movie DB API, along with detailed data such as cast members, genres, and seasons for TV shows.
- Authentication via **JWT** and Google **OAuth2**, with full Spring Security integration.
- Basic caching implemented for faster performance, planning to upgrade to **Redis distributed cache**.
- UI/UX is a work in progress — learning and improving React, CSS, and reusable components structure.
- Clean architecture with DTOs, services, repositories, controllers, Utils and ModelMapper for object mapping.
- Environment variables are securely managed using dotenv, loading them from a .env file (which is excluded from version control and not pushed to Git).
- Utility methods centralized in a utils class (e.g., **createResponse**).
- Routing handled properly with reusable React components for maintainability.
- Using Git for version control.
- Deployment on free hosting providers (like Vercel), with caching strategies to improve speed despite **free tier limitations**.

# Current Status
- The app is still under development.
- Focus has been on backend integration, security, and basic frontend functionalities.
- Learned a lot about frontend development and CSS, and feel more confident with building scalable UI.
- This is my first serious full-stack project after about 3 years of experience mainly backend.
- React experience: about 1.5 months so far, learning by doing with this project.

# Next Steps
- Improve UI/UX and frontend performance.
- Migrate cache to Redis for distributed environment.

# What I Would Improve
- Adopt a **Test-Driven Development (TDD)** approach, as the project currently lacks automated tests (only one controller has partial test coverage).
- Refactor and write **cleaner, more modular code,** with a stronger focus on reusability and abstraction. For example, I faced some issues allowing the Content Manager to add a new movie via the MovieService. Without proper test coverage, modifying the service became risky and required a lot of manual testing, making it harder to ensure stability.