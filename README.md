# Mavericks Learning Management System

A modern, feature-rich Learning Management System (LMS) built with Spring Boot and modern Java practices. This platform enables instructors to create and manage courses, and students to enroll in courses and track their progress.

## 🌟 Features

- **User Management**: Support for multiple user roles (Student, Instructor, Admin)
- **Course Management**: Create, update, and manage courses with sections and various content types
- **Content Delivery**: Support for videos, documents, quizzes, and assignments
- **Progress Tracking**: Track student progress through courses
- **Certificate Generation**: Automatically generate certificates upon course completion
- **Discussion Forums**: Course-specific forums with topics and comments
- **Announcements**: Course-wide announcements
- **Payment Processing**: Secure payment processing for course enrollments
- **Instructor Analytics**: Track instructor earnings and student engagement
- **Authentication & Authorization**: Secure JWT-based authentication with role-based access control
- **OAuth 2.0 Integration**: Support for social login (Google, etc.)

## 🔧 Technologies

- **Java 17**: Modern Java language features
- **Spring Boot 3.x**: Core framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data access layer
- **Hibernate**: ORM for database operations
- **MySQL**: Database
- **JWT**: Token-based authentication
- **OAuth 2.0**: Social login
- **Maven**: Dependency management and build
- **Lombok**: Boilerplate code reduction
- **Flyway**: Database migration
- **ModelMapper**: DTO-Entity mapping

## 🏗️ Architecture

The project follows a layered architecture:

1. **Presentation Layer**: Controllers and DTOs
2. **Business Logic Layer**: Services
3. **Data Access Layer**: Repositories
4. **Domain Layer**: Entities

### Security

- JWT token-based authentication
- OAuth 2.0 for social login
- Role-based access control

### Database

- Relational database (MySQL)
- Flyway for database migrations
- Well-normalized schema with appropriate relationships

## 🚀 Setup & Installation

### Prerequisites

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### Installation Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/AyhamJo7/MavericksLMS-OnlineSchema-JavaSql.git
   cd mavericks-lms
   ```

2. Configure MySQL database:
   ```
   Create a database named 'mavericks_lms'
   ```

3. Update application properties:
   Edit `src/main/resources/application.properties` with your database credentials and other configurations.

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

6. Access the application:
   ```
   API: http://localhost:8080/api/v1
   ```

## 🔍 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── mavericks/
│   │           └── lms/
│   │               ├── config/         # Configuration classes
│   │               ├── controller/     # REST controllers
│   │               ├── dto/            # Data Transfer Objects
│   │               ├── exception/      # Custom exceptions
│   │               ├── model/          # JPA entities
│   │               ├── repository/     # Spring Data repositories
│   │               ├── security/       # Security configuration
│   │               ├── service/        # Business logic
│   │               └── MavericksLmsApplication.java
│   └── resources/
│       ├── db/
│       │   └── migration/      # Flyway migrations
│       ├── static/             # Static resources
│       ├── templates/          # Thymeleaf templates
│       └── application.properties
```

## 💻 Usage Examples

### Creating a Course (Instructor)

1. Authenticate as an instructor
2. Create a new course with details (title, description, price, etc.)
3. Add sections to the course
4. Add content to each section (videos, documents, quizzes, etc.)
5. Publish the course

### Enrolling in a Course (Student)

1. Authenticate as a student
2. Browse available courses
3. Purchase a course
4. Access course content and track progress
5. Participate in discussions and receive a certificate upon completion

## 🛠️ API Documentation

API documentation is available through Swagger UI:
```
http://localhost:8080/api/v1/swagger-ui/index.html
```

## 👥 Who Can Benefit

- **Educational Institutions**: Schools, colleges, and universities
- **Corporate Training**: Companies providing employee training
- **Course Creators**: Individual instructors offering online courses
- **Students**: Learners seeking online educational content

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.
