# Luddy Room Reservation System

A web application for managing room reservations in Indiana University's Luddy Hall. This system allows users to search, filter, and book available rooms based on their needs.

## Live Application
**URL:** https://your-render-url.onrender.com

*Note: The application is hosted on Render's free tier, so initial load time may be slightly longer as the server spins up.*

## Features

### Room Management
- View all available rooms with detailed specifications
- Filter rooms by floor, capacity, and amenities
- Access comprehensive room information including equipment and accessibility features

### Reservation System
- Book rooms by selecting date and time
- Receive booking confirmations
- View and manage your reservations

### User Interface
- Indiana University themed design with official colors
- Responsive layout for different screen sizes
- Intuitive search and filtering

## Technology Stack

**Backend**
- Spring Boot 3.2.0
- Spring Data JDBC
- SQLite Database

**Frontend**
- Thymeleaf Template Engine
- HTML5/CSS3
- Custom IU-branded styling

**Deployment**
- Docker containerization
- Render cloud platform
- Automated CI/CD through GitHub

## Installation and Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Running Locally

1. Clone the repository:
\\\ash
git clone https://github.com/elyryba/luddy-room-reservation.git
cd luddy-room-reservation
\\\

2. Build and run:
\\\ash
mvn spring-boot:run
\\\

3. Access at \http://localhost:8080\

## Project Structure

\\\
src/main/
├── java/com/luddy/roomreservation/
│   ├── controller/     # Request handlers
│   ├── model/          # Data models
│   ├── repository/     # Database operations
│   └── service/        # Business logic
└── resources/
    ├── templates/      # Thymeleaf views
    ├── static/css/     # Stylesheets
    ├── schema.sql      # Database schema
    └── data.sql        # Initial data
\\\

## Database Schema

The application uses SQLite with two main tables:

**Rooms**: Stores room information including number, floor, capacity, and available amenities
**Reservations**: Tracks bookings with user details, time slots, and purpose

## Development Timeline

Development followed an agile approach with three two-week sprints:

**Sprint 1** - Backend foundation and database design  
**Sprint 2** - Core features and user interface  
**Sprint 3** - Booking system and deployment  

## Email Notifications

The system uses Mailtrap for email testing during development. Booking confirmations are sent to a test inbox rather than actual email addresses. For production use, this would be configured with a proper SMTP service.

## Known Limitations

- SQLite's handling of auto-incrementing IDs required some workarounds with Spring Data JDBC
- Free tier hosting may experience cold starts
- Email notifications are currently in test mode

## Future Enhancements

- User authentication system
- Administrative dashboard
- Calendar integration
- Enhanced mobile experience
- Production email service

## Author

Ely Ryba  
Indiana University

## License

Educational project - Indiana University, 2025
