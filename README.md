# Sistema Integrado de Salud (SIS)
<img width="256" height="256" alt="image" src="https://github.com/user-attachments/assets/e99b121c-052c-49d7-a1bf-c85eaedbac23" />

Health Integrated System (SIS) is a web application for medical purposes such as:

1. Storing patient data both locally and in the cloud
2. Consulting information related to a patient
3. Registering procedures related to a patient
4. Managing users (patients, doctors, nurses, administrators)
5. Patient admission and triage processes
6. Medical consultations, diagnoses, and treatments

Our main objective is to provide a solution so that hospitals with poor data management can have access to proper tools so that this process is improved. Since we've yet to have plenty iterations with the client, **this may change quite a lot**, regardless the main objective and course of the project will remain the same.

SDG's that we directly work on:

![Image](https://github.com/user-attachments/assets/9fc67283-a14f-4881-a938-1c425f3a2672)
<img width="150" height="150" alt="Image" src="https://github.com/user-attachments/assets/c629c4f7-1db9-4c1b-a95b-477f63e76c69" />

## Technologies Used
- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Security 6** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **Thymeleaf** - Server-side template engine
- **PostgreSQL 16** - Production database
- **H2 Database** - Testing database
- **Docker & Docker Compose** - Containerization
- **Maven** - Build and dependency management
- **Lombok** - Code generation utilities

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Accessing the Application](#accessing-the-application)
- [Contributors](#contributors)

## Prerequisites
- Java 21 installed
- Maven installed
- Docker and Docker Compose installed and running

## Installation & Setup

1. Clone the repository:
```bash
git clone https://github.com/puj-course/FIS_2530_G2.git
cd FIS_2530_G2
```

2. Compile the project:
```bash
mvn clean package -DskipTests
```

3. Build Docker images:
```bash
docker compose build
```

4. Start the application:
```bash
docker compose up -d
```

5. Stop the application:
```bash
docker compose down
```

## Accessing the Application

The application requires authentication to access its features. The main entry points are:

- `/login` - User authentication
- `/register` - New user registration

Once authenticated, users can access different modules based on their role (patient, doctor, nurse, or administrator). The system provides a complete workflow for patient management, from admission and triage to consultation and treatment.

## Contributors
* Samuel Bonilla Bravo: SCRUM master
* Juan David Acuña Lesmes: Full time developer
* Jonathan Martinez Gomez: Product Owner and developer
* Juan Guillermo Gomez Landinez: Stakeholder and developer
## Integrantes

- Nassin - https://github.com/zeuznnss
