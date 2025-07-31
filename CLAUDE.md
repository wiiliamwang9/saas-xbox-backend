# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a SaaS Platform Backend service initialization project. The repository contains a refined shell script (`initBackendEnv.sh`) that sets up a minimal but complete Java Spring Boot backend environment focused on essential components.

## Architecture & Technology Stack

The initialization script creates a clean Spring Boot project with:

- **Framework**: Spring Boot 3.3.0 with Java 21
- **Build Tool**: Maven 3.9.6
- **Utilities**: Hutool 5.8.28 for common utilities
- **JSON Processing**: Jackson (Spring Boot default)
- **Monitoring**: Spring Boot Actuator for health checks
- **Validation**: Spring Boot Validation starter

## Common Development Commands

### Environment Setup
```bash
# Initialize the complete backend environment
./initBackendEnv.sh
```

### After Initialization (commands created by the script)
```bash
# Start development server
./scripts/dev/start.sh

# Build project
./scripts/dev/build.sh

# Build with Maven directly
mvn clean compile
mvn test
mvn package -DskipTests

# Run application
mvn spring-boot:run
```


## Project Structure (Created by Init Script)

```
saas-platform/
├── src/main/java/com/saas/platform/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── service/         # Business logic layer
│   ├── entity/          # Entity classes
│   ├── dto/             # Data transfer objects
│   ├── util/            # Utility classes
│   └── exception/       # Exception handling
├── src/main/resources/
│   ├── application.yml      # Main configuration
│   └── application-dev.yml  # Development profile
├── scripts/dev/             # Development scripts
└── docs/                    # Project documentation
```

## Key Configuration Details

### Application Configuration
- Base context path: `/api`
- Default port: 8080
- Jackson JSON processing with GMT+8 timezone
- Actuator endpoints for monitoring

### API Standards
- All APIs return unified `Result<T>` wrapper format
- Health check endpoint: `/api/health`
- Standardized error handling with global exception handler

### Environment Profiles
- `dev`: Development environment with debug logging
- Configurable via `application-dev.yml`

## Development Notes

1. **Clean Architecture**: Minimal dependencies focusing on Spring Boot essentials
2. **Monitoring Ready**: Actuator endpoints configured for health checks
3. **Exception Handling**: Global exception handler with unified error responses
4. **Development Scripts**: Automated build and start scripts for development workflow
5. **Documentation**: Chinese documentation indicating Chinese development context

## Initial Setup Process

1. Run `./initBackendEnv.sh` to initialize the complete environment
2. The script installs Java 21 and Maven 3.9.6 locally (user-level installation)
3. Creates the project in `~/wl/code/saas-xbox-backend` (or custom name)
4. Creates full Spring Boot project structure with Maven standard layout
5. Sets up essential configuration files and basic code structure
6. Creates development scripts for building and running the application

## Important Files Created by Init Script

- `pom.xml`: Maven configuration with minimal essential dependencies
- `Application.java`: Spring Boot main class
- `Result.java`: Unified response wrapper for consistent API responses
- `GlobalExceptionHandler.java`: Global exception handling
- `BusinessException.java`: Custom business exception class
- `HealthController.java`: Health check endpoint
- `scripts/dev/start.sh` & `scripts/dev/build.sh`: Development workflow scripts

## Project Documentation

This project includes comprehensive documentation for development:

### Architecture Documents
- `docs/project-architecture-design.md`: Complete system architecture design
  - System architecture diagrams
  - Database design with full table schemas
  - API design specifications
  - Xbox system integration strategy
  - Technology stack and deployment plans

### Development Guides
- `docs/development-task-list.md`: Detailed 9-week development plan
  - Phase-by-phase task breakdown
  - Weekly milestones and deliverables
  - Code quality standards and testing requirements
  - Git workflow and development standards

- `docs/quick-start-guide.md`: Quick start guide for developers
  - Environment setup instructions
  - Project structure explanation
  - Core code examples
  - Common troubleshooting

### Key Features to Implement

**Management Portal:**
- Product Management: IP proxy product configuration and pricing
- IP Pool Management: IP resource allocation and quality monitoring  
- Node Management: Xbox controller integration and monitoring
- Order Management: Order processing and payment integration
- Customer Management: User account and service management
- Staff Management: Admin roles and permissions
- Marketing Management: Campaigns and coupons
- System Management: Configuration and logging

**Customer Portal:**
- User Registration/Login: Account management
- Order Management: Purchase and payment flow
- Static Residential IP: Service instance management
- Personal Center: Profile and usage statistics
- Feedback System: Support tickets and suggestions

### Xbox System Integration

The backend integrates with the existing Xbox node controller system:
- Xbox Controller API: http://localhost:8080/api/v1 (RESTful)
- Xbox Agent API: http://localhost:8081 (Health checks)
- gRPC Integration: Node registration and heartbeat
- Real-time monitoring: System metrics and alerts

### Next Steps

1. Follow the quick-start-guide.md to set up development environment
2. Execute development tasks according to development-task-list.md
3. Implement features following the architecture design specifications
4. Integrate with Xbox system using the planned API mappings