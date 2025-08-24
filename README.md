💍 Capstone - Documentation
🚀 Sprint Documentation: Digit Matrimony System (MVP)
📌 Project Overview
Project Title: Digit Matrimony System Purpose: To build a secure and scalable backend system for matrimonial matchmaking, enabling users to register, discover matches, communicate, and manage feedback and reports. The system supports multiple roles and subscription-based features.

Target Users:

Registered Users

Family Members

Managers

Admins

🎯 Sprint Goal
Deliver a Minimum Viable Product (MVP) that supports:

✅ User registration and profile creation

✅ Match discovery and interest exchange

✅ Messaging between matched users

✅ Role-based access control (User, Family, Manager, Admin)

✅ Feedback and reporting mechanisms

✅ Subscription-based feature access

✅ Analytics dashboard for admins

🧱 Sprint Duration
Start Date	End Date	Duration
Aug 14, 2025	Aug 22, 2025	8 days
📌 User Stories
markdown
| **ID** | **Role**         | **Goal**                                                        | **Priority** | **Est. Time** |
|--------|------------------|------------------------------------------------------------------|--------------|---------------|
| US1    | User             | Register and create profile                                      | High         | 1 day         |
| US2    | User             | View and send interest to matches                                | High         | 1.5 days      |
| US3    | User             | Message matched users                                            | High         | 1 day         |
| US4    | Family Member    | View profiles and submit feedback                                | Medium       | 1 day         |
| US5    | Manager          | Review matches and add notes                                     | Medium       | 1 day         |
| US6    | Admin            | View analytics and monitor activity                              | High         | 1 day         |
| US7    | User             | Report inappropriate profiles                                    | High         | 1 day         |
| US8    | System           | Escalate reports based on subscription status                    | Medium       | 1 day         |
⚙️ System Architecture
Tech Stack:

Language: Java / Spring Boot

Database: PostgreSQL

Authentication: JWT + Role-based via Spring Security

API Communication: REST

Build Tool: Maven

Testing: JUnit + Mockito

Documentation: README + Hoppscotch/Postman

🧩 Core Modules
User & Role Management

Role-based login (Admin=1, Manager=2, User=3, Family=4)

Linked accounts (e.g., family linked to user)

Profile & Preferences

Education, caste, religion, income, location

Match preferences (age, caste, location, etc.)

Matchmaking & Interests

View and send interest to up to 3 matches

Match score via suggestion engine

Messaging

Chat between matched users

Timestamped message history

Feedback & Reports

Family feedback on viewed profiles

User-generated reports with escalation logic

Subscription Management

Premium plans unlock escalated reporting

Payment tracking and plan duration

Manager & Admin Actions

Manager notes and match actions

Admin analytics dashboard

Analytics

Regional user stats, revenue, match activity

📄 Sample REST Endpoints
markdown
| **Endpoint**                  | **Method** | **Role**   | **Description**                        |
|------------------------------|------------|------------|----------------------------------------|
| /api/users/register          | POST       | Public     | Register new user                      |
| /api/users/login             | POST       | Public     | Login and receive JWT                  |
| /api/profiles/{id}           | GET/PUT    | User       | View or update profile                 |
| /api/preferences             | POST       | User       | Set match preferences                  |
| /api/matches/view            | GET        | User       | View suggested matches                 |
| /api/interests/send/{id}     | POST       | User       | Send interest                          |
| /api/messages/{matchId}      | GET/POST   | User       | Chat with matched user                 |
| /api/family/feedback         | POST       | Family     | Submit feedback                        |
| /api/reports                 | POST       | User       | Report another user                    |
| /api/manager/actions         | PATCH      | Manager    | Perform match actions                  |
| /api/admin/analytics         | GET        | Admin      | View system analytics                  |
✅ Acceptance Criteria
[ ] Users can register and log in with role-based access

[ ] Profiles and preferences are stored and retrievable

[ ] Match suggestions and interest flow are functional

[ ] Messaging works only between matched users

[ ] Family feedback is linked to match and profile

[ ] Reports escalate based on subscription type

[ ] Admin dashboard shows analytics snapshot

📊 Deliverables
REST API endpoints

PostgreSQL schema

JWT-based authentication

Unit tests for all services

Postman collection for API testing

README with setup instructions

🧠 Learning Objectives
Implement layered architecture with Spring Boot

Use JWT for secure role-based access

Apply business logic for matchmaking and escalation

Integrate feedback and reporting workflows

Document APIs using Hoppscotch and README

Practice agile-style sprint planning and delivery

📁 Project Structure
/Matrimony-Digit/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── digit/
│   │   │       └── matrimony/
│   │   │           ├── controller/         # REST controllers
│   │   │           ├── service/            # Business logic
│   │   │           ├── repository/         # Spring Data JPA interfaces
│   │   │           ├── entity/             # JPA entity classes
│   │   │           ├── dto/                # Data Transfer Objects (e.g., FamilyFeedbackDTO)
│   │   │           ├── config/             # Security, JWT, and app config
│   │   │           └── exception/          # Custom exceptions
│   │   └── resources/
│   │       ├── application.yml             # Environment config
│   │       └── data.sql                    # Optional mock data
│
├── test/
│   └── java/
│       └── digit/
│           └── matrimony/                  # Unit and integration tests
│
├── pom.xml                                 # Maven build file
├── README.md                               # Project overview and setup
└── postman-collection/                     # API testing collection (if applicable)

