💍 Digit Matrimony System
🚀 Capstone Sprint Documentation – MVP Release
📌 Project Overview
🎯 Title: Digit Matrimony System 🔐 Purpose: Build a secure, scalable backend for matrimonial matchmaking. Supports:

Role-based access (User, Family, Manager, Admin)

Match discovery, messaging, feedback, reporting

Subscription-based features and analytics

👥 Target Users:

👤 Registered Users

👪 Family Members

🧑‍💼 Managers

🛡️ Admins

🎯 Sprint Goal
Deliver a fully functional Minimum Viable Product (MVP) with:

✅ User registration & profile creation ✅ Match discovery & interest exchange ✅ Messaging between matched users ✅ Role-based access control ✅ Feedback & reporting workflows ✅ Subscription-based escalation ✅ Admin analytics dashboard

🗓️ Sprint Timeline
📅 Start Date	📅 End Date	⏱️ Duration
Aug 14, 2025	Aug 22, 2025	8 Days
📋 User Stories
🆔	👤 Role	📝 Description	🔥 Priority	⏳ Est. Time
US1	User	Register and create profile	High	1 day
US2	User	View and send interest to matches	High	1.5 days
US3	User	Message matched users	High	1 day
US4	Family Member	View profiles and submit feedback	Medium	1 day
US5	Manager	Review matches and add notes	Medium	1 day
US6	Admin	View analytics and monitor activity	High	1 day
US7	User	Report inappropriate profiles	High	1 day
US8	System	Escalate reports based on subscription status	Medium	1 day
⚙️ System Architecture
🧰 Tech Stack:

🧑‍💻 Language: Java + Spring Boot

🗄️ Database: PostgreSQL

🔐 Auth: JWT + Spring Security

🔗 API: REST

🛠️ Build Tool: Maven

🧪 Testing: JUnit + Mockito

📚 Docs: README + Hoppscotch/Postman

🧩 Core Modules
🔐 User & Role Management
Role-based login (Admin=1, Manager=2, User=3, Family=4)

Linked accounts (e.g., family linked to user)

🧬 Profile & Preferences
Education, caste, religion, income, location

Match preferences (age, caste, location, etc.)

💘 Matchmaking & Interests
View/send interest to up to 3 matches

Match score via suggestion engine

💬 Messaging
Chat between matched users

Timestamped message history

📝 Feedback & Reports
Family feedback on viewed profiles

User-generated reports with escalation logic

💎 Subscription Management
Premium plans unlock escalated reporting

Payment tracking and plan duration

🧑‍💼 Manager & Admin Actions
Manager notes and match actions

Admin analytics dashboard

📊 Analytics
Regional user stats, revenue, match activity

📄 Sample REST Endpoints
🔗 Endpoint	📥 Method	👤 Role	📌 Description
/api/users/register	POST	Public	Register new user
/api/users/login	POST	Public	Login and receive JWT
/api/profiles/{id}	GET/PUT	User	View or update profile
/api/preferences	POST	User	Set match preferences
/api/matches/view	GET	User	View suggested matches
/api/interests/send/{id}	POST	User	Send interest
/api/messages/{matchId}	GET/POST	User	Chat with matched user
/api/family/feedback	POST	Family	Submit feedback
/api/reports	POST	User	Report another user
/api/manager/actions	PATCH	Manager	Perform match actions
/api/admin/analytics	GET	Admin	View system analytics
✅ Acceptance Criteria
[x] Role-based registration and login

[x] Profile and preference management

[x] Match suggestions and interest flow

[x] Messaging between matched users only

[x] Family feedback linked to profiles

[x] Escalated reports based on subscription

[x] Admin dashboard with analytics snapshot

📦 Deliverables
🔗 REST API endpoints

🗄️ PostgreSQL schema

🔐 JWT-based authentication

🧪 Unit tests for all services

📮 Postman collection for API testing

📘 README with setup instructions

🧠 Learning Objectives
🧱 Implement layered architecture with Spring Boot

🔐 Use JWT for secure role-based access

🧠 Apply business logic for matchmaking and escalation

📝 Integrate feedback and reporting workflows

📚 Document APIs using Hoppscotch and README

🏃 Practice agile-style sprint planning and delivery

📁 Project Structure
plaintext
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
└── postman-collection/                     # API testing collection (if app
