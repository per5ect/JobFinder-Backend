# JobFinder Back-End

![GitHub repo size](https://img.shields.io/github/repo-size/per5ect/JobFinder-FrontEnd)
![GitHub stars](https://img.shields.io/github/stars/per5ect/JobFinder-FrontEnd?style=social)

Software Requirements Specification (SRS):
https://github.com/per5ect/JobFinder-Backend/blob/main/SRS%20JobFinder.pdf

Software Design Document (SDD):
https://github.com/per5ect/JobFinder-Backend/blob/main/SDD%20Jobfinder.pdf

Figma design: 
https://www.figma.com/design/CLQlBV5p9wejuXqliZ6CZE/JobFinder?node-id=0-1&t=nKr3H12D26vQ6Lv6-1


## Description

**JobFinder Backend** is the server-side component for the JobFinder web application, built with Java Spring Boot.  
It provides RESTful APIs for job search, user authentication, and document management, integrating with Apache PDFBox for parsing PDFs, Gemini API for advanced document processing, and Cloudinary API for image and file storage.

## Features

- 🔒 User authentication and authorization
- 💼 Job posting and search APIs
- 📝 Resume/document upload and parsing (PDF support via Apache PDFBox)
- 🤖 Gemini API integration for document analysis
- ☁️ Cloudinary API for file and image uploads
- 📄 CRUD operations for jobs and users
- 📊 Robust error handling and logging

## Technologies Used

- [Java Spring Boot](https://spring.io/projects/spring-boot)
- [Apache PDFBox](https://pdfbox.apache.org/) (PDF parser)
- [Gemini API](https://ai.google.dev/gemini-api/docs) (document processing)
- [Cloudinary API](https://cloudinary.com/documentation) (cloud file storage)
- [Maven](https://maven.apache.org/)
- [DB PstgreSQL](https://www.postgresql.org/) (database)


## Usage

- Open application in your browser.
- Use the search bar to find jobs by keyword or location.
- Apply filters to narrow down results.
- Click on job listings to view detailed information.
- Analyzing a resume
- Obtaining suitable vacancies after analysis
- Creation of vacancies by the firm
- Administering the application by admin



<img width="1437" height="698" alt="image" src="https://github.com/user-attachments/assets/41714ff5-473b-429b-9ca3-08adb2006714" />
<img width="1430" height="685" alt="image" src="https://github.com/user-attachments/assets/1c867c67-3d4b-49e8-a0a6-91ced0df3e6b" />
<img width="1431" height="700" alt="image" src="https://github.com/user-attachments/assets/7c88f066-2a6d-4615-a1ce-e0fef6210fba" />
<img width="1434" height="678" alt="image" src="https://github.com/user-attachments/assets/9c1247a0-a0f6-45d2-94fa-fd6abda1bfcd" />
<img width="1162" height="695" alt="image" src="https://github.com/user-attachments/assets/49a7b69e-14ec-47e1-a2de-1bb0dda2f967" />
<img width="1436" height="698" alt="image" src="https://github.com/user-attachments/assets/d0c255bb-9134-49ba-88f5-c303426c5faa" />


## Project Structure

```
Directory structure:
└── per5ect-jobfinder-backend/
    ├── mvnw
    ├── mvnw.cmd
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── com/
    │   │   │       └── jobfinder/
    │   │   │           └── backend/
    │   │   │               └── jobfinderbackend/
    │   │   │                   ├── JobFinderBackendApplication.java
    │   │   │                   ├── config/
    │   │   │                   │   ├── AdminConfiguration.java
    │   │   │                   │   ├── ApplicationConfiguration.java
    │   │   │                   │   ├── CloudinaryConfiguration.java
    │   │   │                   │   ├── EmailConfiguration.java
    │   │   │                   │   ├── JWTAuthenticationFilter.java
    │   │   │                   │   ├── RoleSeederConfiguration.java
    │   │   │                   │   └── SecurityConfiguration.java
    │   │   │                   ├── controllers/
    │   │   │                   │   ├── AdminController.java
    │   │   │                   │   ├── CompanyAuthenticationController.java
    │   │   │                   │   ├── CompanyController.java
    │   │   │                   │   ├── PublicController.java
    │   │   │                   │   ├── UserAuthenticationController.java
    │   │   │                   │   └── UserController.java
    │   │   │                   ├── dto/
    │   │   │                   │   ├── CompanyDetailsDTO.java
    │   │   │                   │   ├── CreateTechnologyDTO.java
    │   │   │                   │   ├── CreateVacancyDTO.java
    │   │   │                   │   ├── LoginCompanyDTO.java
    │   │   │                   │   ├── LoginResponseDTO.java
    │   │   │                   │   ├── LoginUserDTO.java
    │   │   │                   │   ├── RegisterCompanyDTO.java
    │   │   │                   │   ├── RegisterUserDTO.java
    │   │   │                   │   ├── ResponsesToVacancyDTO.java
    │   │   │                   │   ├── ResumeAnalysisResponseDTO.java
    │   │   │                   │   ├── TechnologyDTO.java
    │   │   │                   │   ├── UserApplicationsDTO.java
    │   │   │                   │   ├── UserCoverLetterDTO.java
    │   │   │                   │   ├── UserDetailsDTO.java
    │   │   │                   │   ├── VacancyDetailsDTO.java
    │   │   │                   │   ├── VacancyFilterCriteriaDTO.java
    │   │   │                   │   ├── VerifyCompanyDTO.java
    │   │   │                   │   └── VerifyUserDTO.java
    │   │   │                   ├── models/
    │   │   │                   │   ├── ApplicationStatusEnum.java
    │   │   │                   │   ├── Company.java
    │   │   │                   │   ├── Resume.java
    │   │   │                   │   ├── Role.java
    │   │   │                   │   ├── RoleEnum.java
    │   │   │                   │   ├── Technology.java
    │   │   │                   │   ├── User.java
    │   │   │                   │   ├── UserFavoriteVacancy.java
    │   │   │                   │   ├── Vacancy.java
    │   │   │                   │   └── VacancyApplications.java
    │   │   │                   ├── repository/
    │   │   │                   │   ├── CompanyRepository.java
    │   │   │                   │   ├── ResumeRepository.java
    │   │   │                   │   ├── RoleRepository.java
    │   │   │                   │   ├── TechnologyRepository.java
    │   │   │                   │   ├── UserFavoriteVacancyRepository.java
    │   │   │                   │   ├── UserRepository.java
    │   │   │                   │   ├── VacancyApplicationsRepository.java
    │   │   │                   │   └── VacancyRepository.java
    │   │   │                   ├── services/
    │   │   │                   │   ├── AdminService.java
    │   │   │                   │   ├── CompanyAuthenticationService.java
    │   │   │                   │   ├── CompanyService.java
    │   │   │                   │   ├── EmailService.java
    │   │   │                   │   ├── JWTService.java
    │   │   │                   │   ├── ResumeAnalysisService.java
    │   │   │                   │   ├── ResumeService.java
    │   │   │                   │   ├── TechnologyService.java
    │   │   │                   │   ├── UserAuthenticationService.java
    │   │   │                   │   ├── UserService.java
    │   │   │                   │   ├── VacancyApplicationsService.java
    │   │   │                   │   ├── VacancyFilterService.java
    │   │   │                   │   ├── VacancyMatchingService.java
    │   │   │                   │   └── VacancyService.java
    │   │   │                   └── utils/
    │   │   │                       ├── GeminiClient.java
    │   │   │                       └── ResumeParser.java
    │   │   └── resources/
    │   │       └── application.properties
    │   └── test/
    │       └── java/
    │           └── com/
    │               └── jobfinder/
    │                   └── backend/
    │                       └── jobfinderbackend/
    │                           └── JobFinderBackendApplicationTests.java
    └── .mvn/
        └── wrapper/
            └── maven-wrapper.properties


```
