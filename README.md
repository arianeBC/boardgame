# BoardGame Project with CI/CD Pipeline

Welcome to the **BoardGame** project! This repository demonstrates a Java-based fullstack application for board game
management, featuring an automated CI/CD pipeline designed for reliable code integration, testing, security checks, and
deployment. This README provides an overview of the pipeline, with a high-level introduction to the project.

## Project Overview

The **BoardGame** application is a Java-based fullstack application, that allows users to register accounts, manage game
data , and leave comments on their experiences.

### Tech Stack

- **Backend**: Java, Spring Boot
- **Frontend**: JavaScript, HTML, CSS
- **Database**: PostgreSQL
- **Containerization & Orchestration**: Docker, Kubernetes

## CI/CD Pipeline Overview

The CI/CD pipeline for this project is structured to provide automated code quality checks, security scans, and
deployments. This ensures that each code change is thoroughly validated and securely deployed.

### Pipeline Steps

1. **Code Versioning**

- **Tool**: GitHub
- **Description**: Source code is hosted on GitHub, where changes are committed and versioned.

2. **Automated Build Trigger**

- **Tool**: Jenkins
- **Description**: Jenkins triggers the pipeline whenever new code is pushed to the repository, initiating an automated
  build process.

3. **Code Compilation & Unit Testing**

- **Tool**: Maven
- **Description**: Maven compiles the code and runs unit tests to verify functionality and catch potential issues early.

4. **Code Quality Analysis**

- **Tool**: SonarQube
- **Description**: SonarQube analyzes the code for quality metrics like maintainability, reliability, and coverage.

5. **Dependency Vulnerability Scanning**

- **Tool**: Trivy
- **Description**: Trivy scans project dependencies to identify and report any known vulnerabilities.

6. **Application Packaging**

- **Tool**: Maven
- **Description**: Maven packages the application into a deployable artifact, ready for further stages.

7. **Artifact Repository Storage**

- **Tool**: Nexus
- **Description**: The packaged artifact is stored in Nexus, providing a centralized repository for version management
  and future deployments.

8. **Docker Image Creation**

- **Tool**: Docker
- **Description**: A Docker image of the application is built and tagged, preparing it for deployment.

9. **Docker Image Vulnerability Scan**

- **Tool**: Trivy
- **Description**: Trivy scans the Docker image for vulnerabilities, ensuring image security before deployment.

10. **Docker Image Publishing**

- **Tool**: Docker Hub
- **Description**: The Docker image is pushed to Docker Hub for accessible storage and versioning.

11. **Deployment to Kubernetes**

- **Tool**: Kubernetes
- **Description**: The Docker image is deployed to a Kubernetes cluster, making the application available for use.

12. **Post-Deployment Security Audit**

- **Tool**: Kubeaudit
- **Description**: Kubeaudit performs a security scan on the deployed resources to ensure compliance and secure
  configuration.

13. **Notification of Pipeline Completion**

- **Tool**: Gmail
- **Description**: Upon successful completion, a status email is sent to notify stakeholders of the pipeline results.

![BoardGame Logo](images/pipeline.svg)

## Getting Started

To run this project locally or contribute, clone the repository and follow the setup instructions below.

### Prerequisites

- **Java 17+**
- **Maven**
- **Docker**
- **Kubernetes cluster** (for deployment)
- **Jenkins, SonarQube, Trivy, Nexus** (for pipeline setup)

### Installation and Setup

1. Clone the repository:

```bash
git clone https://github.com/arianeBC/boardgame.git
cd boardgame
```

2. Install dependencies:

```bash
mvn install
```

3. Configure the CI/CD pipeline in Jenkins using the pipeline script (`Jenkinsfile`) in this repository.

4. Deploy the application by triggering the pipeline in Jenkins.
