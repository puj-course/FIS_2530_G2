# Sistema Integrado de Salud (SIS)
<img width="256" height="256" alt="image" src="https://github.com/user-attachments/assets/e99b121c-052c-49d7-a1bf-c85eaedbac23" />

Health Integrated System (SIS) is a desktop application for medical purposes such as:

1. Storing patient data both locally and in the cloud
2. Consulting information related to a patient
3. Registering procedures related to a patient

SDGs directly addressed:

![Image](https://github.com/user-attachments/assets/9fc67283-a14f-4881-a938-1c425f3a2672)
<img width="150" height="150" alt="Image" src="https://github.com/user-attachments/assets/c629c4f7-1db9-4c1b-a95b-477f63e76c69" />

## Table of Contents
- [Prerequisites](#prerequisites)
- [Database Setup with Docker](#database-setup-with-docker)
- [Installation](#installation)
- [Usage](#usage)
- [Contributors](#contributors)

## Prerequisites
- Java 17 installed
- Maven installed
- Docker installed and running

## Database Setup with Docker
To run a PostgreSQL database using Docker, execute:
```bash
docker run --name sis_postgres -e POSTGRES_DB=sis_db -e POSTGRES_USER=sis_user -e POSTGRES_PASSWORD=sis_password -p 5432:5432 -d postgres:latest
```
This will create a PostgreSQL container with:
- Database: `sis_db`
- User: `sis_user`
- Password: `sis_password`
- Port: `5432`

Configure the following environment variables for the application:
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/sis_db`
- `SPRING_DATASOURCE_USERNAME=sis_user`
- `SPRING_DATASOURCE_PASSWORD=sis_password`

## Installation
1. Clone the repository:
```bash
git clone https://github.com/puj-course/FIS_2530_G2.git
```
2. Compile the project using Maven:
```bash
mvn clean package
```

## Usage
To run the project, use the following command:
```bash
java -jar target/SIS-0.0.1-SNAPSHOT.jar
```

## Contributors
* Samuel Bonilla Bravo: SCRUM master
* Juan David Acuña Lesmes: Full time developer
* Jonathan Martinez Gomez: Product Owner and developer
* Juan Guillermo Gomez Landinez: Stakeholder and developer
