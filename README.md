# Luddy Room Reservation System

A web application for reserving rooms in the Luddy building.

## Features
- Browse available rooms
- Search and filter by capacity, floor, and amenities
- Book rooms with date/time selection
- View your reservations

## Email Notifications
**Note:** This system uses Mailtrap for email testing. Confirmation emails are sent to Mailtrap's test inbox, NOT to your actual email address. This is for development/testing purposes only.

To receive emails in Mailtrap:
1. Sign up at https://mailtrap.io
2. Check the Mailtrap inbox to see booking confirmations

## Running the Application
1. Clone the repository
2. Run: mvn spring-boot:run
3. Open browser to http://localhost:8080

## Technology Stack
- Spring Boot 3.2.0
- SQLite Database
- Thymeleaf Templates
- Maven
