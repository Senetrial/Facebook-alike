# Facebook Alike Prototype

A simple, robust, and extensible full-stack Facebook-like application built with modern technologies. This project serves as a prototype demonstrating core social media functionalities with a focus on clean code and maintainability.

## 🚀 Key Features

- **Dynamic Feed**: Browse posts with an infinite scroll (lazy loading) mechanism.
- **Rich Posts**: Support for text-only posts and posts with image uploads.
- **Interactive Comments**: Paginated comment sections with the ability to "roll out" more comments or "roll back" to a collapsed view.
- **Reactions**: Like and Dislike posts with real-time count updates and database persistence.
- **Responsive Design**: A clean, Facebook-inspired UI that works across different screen sizes.

## 🛠 Tech Stack

### Backend
- **Language**: Kotlin
- **Framework**: Spring Boot 4.1.0 (running on Java 25)
- **Database**: MongoDB
- **Testing**: JUnit 5, Mockito, Spring Boot Test

### Frontend
- **Framework**: React 19 (Vite + TypeScript)
- **Styling**: CSS3 (Flexbox/Grid)
- **State Management**: React Hooks (useState, useEffect)
- **Testing**: Vitest, React Testing Library, MSW (Mock Service Worker)

## 🧠 Design Principles

This project strictly adheres to two fundamental software engineering principles:

### KISS (Keep It Simple, Stupid)
- **No Over-Engineering**: We use standard Spring Data repositories and native React state management instead of heavy external libraries where they aren't needed.
- **Readable APIs**: REST endpoints are designed to be intuitive (e.g., `GET /api/posts`, `POST /api/comments`).
- **Lean Domain Model**: Entities like `Post`, `Comment`, and `Like` contain only the necessary fields to fulfill the requirements.

### ETC (Easier To Change)
- **Decoupled Logic**: Services are separated from Controllers, and the Frontend is fully decoupled from the Backend via a clear API contract.
- **Comprehensive Testing**: With a 100% pass rate on both unit and integration tests, refactoring can be done with high confidence.
- **Type Safety**: The use of Kotlin and TypeScript ensures that data structures are consistent across the entire stack, making changes easier to track and implement.

## 🏁 Getting Started

### Prerequisites
- **Java**: Version 25
- **Node.js**: Version 24 or higher
- **MongoDB**: Running locally on the default port `27017`

### Running the Application

1. **Start the Backend**:
   ```bash
   ./gradlew bootRun
   ```
   The API will be available at `http://localhost:8080`.

2. **Start the Frontend**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   The application will be available at `http://localhost:5173`.

## 🧪 Testing

The project includes a full suite of automated tests to ensure stability.

- **Backend Tests**:
  ```bash
  ./gradlew test
  ```
- **Frontend Tests**:
  ```bash
  cd frontend
  npm test
  ```

---
Developed as a high-quality prototype for a social media platform.
