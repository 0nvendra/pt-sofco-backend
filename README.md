# 🛡️ Attendance Portal Backend (Spring Boot)

Welcome to the **Attendance Portal Backend**, a robust and secure REST API built with Java Spring Boot. This system serves as the backbone for the Attendance Mobile Application, handling user authentication, attendance management with facial verification, and geolocation tracking.

---

## 🚀 Technology Stack

- **Core Framework**: Java 21 & Spring Boot 4.0.5
- **Security**: Spring Security with JWT (JSON Web Token)
- **Database**: PostgreSQL (Cloud-hosted on Neon)
- **Storage**: Firebase Cloud Storage (via Firebase Admin SDK)
- **Migration**: Flyway (ready for production migrations)
- **Build Tool**: Maven
- **Architecture**: Layered (Controller, Service, Repository, DTO, Model)

---

## ✨ Key Features

- **JWT Authentication**: Secure login and session management.
- **Attendance Submission**:
  - Captures and stores user face photos in Firebase.
  - Validates GPS coordinates (Latitude/Longitude).
  - High precision coordinate tracking.
- **User Management**: Profile data including roles (Admin/User).
- **Attendance History**: Retrieves personal and global attendance logs.
- **Storage Integration**: Seamlessly integrates with Firebase for image uploads.

---

## 🛠️ Getting Started

### Prerequisites

- **JDK 21** or higher.
- **Maven 3.x**.
- **PostgreSQL** (Optional if using the cloud Neon DB).

### Configuration

1. **Firebase Setup**:
   - Place your `serviceAccountKey.json` inside `src/main/resources/`.
   - Update `FirebaseStorageService.java` with your Firebase Bucket name if necessary.

2. **Database Configuration**:
   The application is currently configured to connect to a Neon PostgreSQL instance. You can modify these in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://your-db-url
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

### Running the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## 📚 API Documentation

### Auth Endpoints

- `POST /api/auth/login`: Authenticate user and receive JWT.
- `POST /api/auth/register`: Create a new user account.

### Attendance Endpoints

- `POST /api/attendance/submit`: Submit attendance (Multipart Form Data).
- `GET /api/attendance/history`: View attendance logs.

---

## 📁 Project Structure

```text
📂 backend (Java Spring Boot)
│   └── 📂 src/main/java/com/example/test_pt_sofco_graha_gaji/id
│       ├── 📂 config         # Firebase & App Configurations
│       ├── 📂 controller     # REST API Endpoints
│       ├── 📂 dto            # Data Transfer Objects
│       ├── 📂 exception      # Global Exception Handling
│       ├── 📂 model          # JPA Entities (PostgreSQL)
│       ├── 📂 repository     # Data Access Layer
│       ├── 📂 security       # JWT & Spring Security
│       └── 📂 service        # Business Logic
```

---

_Developed for PT Sofco Graha Gaji Technical Assessment._
