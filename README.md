# Spring Boot Microservices Architecture

A comprehensive microservices architecture implementation using Spring Boot 3.5.6, Spring Cloud 2025.0.0, and reactive programming with WebFlux.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Services](#services)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Monitoring](#monitoring)
- [Contributing](#contributing)

## 🎯 Overview

This project demonstrates a modern microservices architecture built with Spring Boot, featuring:

- **Service Discovery** with Netflix Eureka
- **API Gateway** with Spring Cloud Gateway
- **Reactive Programming** with Spring WebFlux
- **Load Balancing** with Spring Cloud LoadBalancer
- **Inter-service Communication** using Feign and WebClient
- **Database Integration** with JPA and MySQL

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Client    │    │   Mobile App    │    │  External APIs  │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │    Gateway Server         │
                    │    (Port: 8090)          │
                    │    - Routing             │
                    │    - Load Balancing      │
                    │    - Global Filters      │
                    └─────────────┬─────────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                  │
   ┌──────────▼──────────┐      │       ┌─────────▼─────────┐
   │   Products Service  │      │       │   Items Service   │
   │   (Port: Random)    │      │       │   (Port: 8002)    │
   │   - JPA/MySQL       │      │       │   - WebClient     │
   │   - CRUD Operations │      │       │   - Feign Client  │
   └─────────────────────┘      │       └───────────────────┘
                                │
                    ┌───────────▼───────────┐
                    │   Eureka Server       │
                    │   (Port: 8761)       │
                    │   - Service Registry  │
                    │   - Health Monitoring │
                    └───────────────────────┘
```

## 🚀 Services

### 1. Eureka Server (`eureka-server`)
- **Port**: 8761
- **Purpose**: Service registry and discovery
- **Technology**: Spring Cloud Netflix Eureka Server
- **URL**: http://localhost:8761

### 2. Gateway Server (`msvc-gateway-server`)
- **Port**: 8090
- **Purpose**: API Gateway with routing and load balancing
- **Technology**: Spring Cloud Gateway WebFlux
- **Features**:
  - Dynamic routing to microservices
  - Global and custom filters
  - Request/Response transformation
  - Load balancing

### 3. Products Service (`msvc-products`)
- **Port**: Random (auto-assigned)
- **Purpose**: Product catalog management
- **Technology**: Spring Boot Web + JPA
- **Database**: MySQL (`db_springboot_cloud`)
- **Features**:
  - CRUD operations for products
  - Database persistence
  - Eureka client registration

### 4. Items Service (`msvc-items`)
- **Port**: 8002
- **Purpose**: Business logic aggregator
- **Technology**: Spring Boot WebFlux + Feign
- **Features**:
  - Consumes Products Service
  - Reactive programming
  - Feign and WebClient implementations
  - Load balancing

## 🛠️ Technologies

| Component | Technology | Version |
|-----------|------------|---------|
| **Framework** | Spring Boot | 3.5.6 |
| **Cloud** | Spring Cloud | 2025.0.0 |
| **Language** | Java | 21 |
| **Build Tool** | Maven | 3.9.11 |
| **Database** | MySQL | Latest |
| **Service Discovery** | Netflix Eureka | Latest |
| **Gateway** | Spring Cloud Gateway | Latest |
| **Reactive** | Spring WebFlux | Latest |
| **HTTP Client** | OpenFeign | Latest |

## 📋 Prerequisites

Before running this project, ensure you have:

- **Java 21** or higher
- **Maven 3.9+** (or use included Maven wrapper)
- **MySQL Server** running on port 3306
- **Git** for version control

### Database Setup

1. Install and start MySQL Server
2. Create database:
```sql
CREATE DATABASE db_springboot_cloud;
```

3. Configure credentials in `msvc-products/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_springboot_cloud
spring.datasource.username=root
spring.datasource.password=admin
```

## 🚀 Getting Started

### Option 1: Using Maven Wrapper (Recommended)

1. **Clone the repository**
```bash
git clone <repository-url>
cd "Spring Webflux"
```

2. **Start services in order**

**Step 1: Start Eureka Server**
```bash
cd eureka-server
./mvnw spring-boot:run
# Wait for startup (check http://localhost:8761)
```

**Step 2: Start Products Service**
```bash
cd ../msvc-products
./mvnw spring-boot:run
# Multiple instances can be started on different ports
```

**Step 3: Start Items Service**
```bash
cd ../msvc-items
./mvnw spring-boot:run
```

**Step 4: Start Gateway Server**
```bash
cd ../msvc-gateway-server
./mvnw spring-boot:run
```

### Option 2: Using IDE

1. Import the project as a Maven multi-module project
2. Start each service individually through your IDE's run configuration

### Option 3: Using JAR files

```bash
# Build all services
mvn clean package -DskipTests

# Run each service
java -jar eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar
java -jar msvc-products/target/msvc-products-0.0.1-SNAPSHOT.jar
java -jar msvc-items/target/msvc-items-0.0.1-SNAPSHOT.jar
java -jar msvc-gateway-server/target/msvc-gateway-server-0.0.1-SNAPSHOT.jar
```

## ⚙️ Configuration

### Service Ports

| Service | Port | Environment Variable |
|---------|------|---------------------|
| Eureka Server | 8761 | Fixed |
| Gateway Server | 8090 | Fixed |
| Products Service | Random | `PORT` (default: random) |
| Items Service | 8002 | Fixed |

### Environment Variables

#### Products Service
```properties
PORT=0                          # Random port assignment
SPRING_DATASOURCE_URL          # MySQL connection URL
SPRING_DATASOURCE_USERNAME     # Database username
SPRING_DATASOURCE_PASSWORD     # Database password
```

#### Gateway Server
```yaml
spring.cloud.gateway.routes[*] # Route configurations
```

## 📖 API Documentation

### Gateway Endpoints

All requests go through the Gateway Server at `http://localhost:8090`

#### Products API
```
GET    /api/products/       # List all products
GET    /api/products/{id}   # Get product by ID
```

#### Items API
```
GET    /api/items/          # List all items (aggregated from products)
GET    /api/items/{id}      # Get item by ID
```

### Direct Service Endpoints

#### Eureka Dashboard
```
GET    http://localhost:8761    # Eureka Server dashboard
```

#### Products Service (Direct)
```
GET    http://localhost:{random_port}/     # List products
GET    http://localhost:{random_port}/{id} # Get product
```

#### Items Service (Direct)
```
GET    http://localhost:8002/              # List items
GET    http://localhost:8002/{id}          # Get item
```

### Example Requests

**Get all products through gateway:**
```bash
curl http://localhost:8090/api/products/
```

**Get all items through gateway:**
```bash
curl http://localhost:8090/api/items/
```

**Get specific item:**
```bash
curl http://localhost:8090/api/items/1
```

## 🧪 Testing

### Running Unit Tests

```bash
# Test all modules
mvn test

# Test specific module
cd msvc-products
mvn test
```

### Integration Testing

1. Ensure all services are running
2. Test service discovery:
```bash
curl http://localhost:8761/eureka/apps
```

3. Test gateway routing:
```bash
curl http://localhost:8090/api/products/
curl http://localhost:8090/api/items/
```

### Load Testing

Start multiple instances of the Products Service to test load balancing:

```bash
# Terminal 1
cd msvc-products
PORT=8001 ./mvnw spring-boot:run

# Terminal 2
cd msvc-products
PORT=8003 ./mvnw spring-boot:run
```

## 📊 Monitoring

### Eureka Dashboard
- URL: http://localhost:8761
- View registered services and health status

### Application Logs
Each service provides detailed logging for:
- Service registration/deregistration
- HTTP requests and responses
- Load balancer decisions
- Filter executions

### Health Checks
```bash
# Check individual service health
curl http://localhost:8002/actuator/health
curl http://localhost:8090/actuator/health
```

## 🔧 Development

### Adding a New Service

1. Create a new Spring Boot project
2. Add Eureka Client dependency
3. Configure `application.properties`:
```properties
spring.application.name=your-service-name
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

4. Add gateway routing in `msvc-gateway-server/application.yml`

### Custom Filters

The project includes examples of:
- **Global Filters**: `SampleGlobalFilter`
- **Custom Gateway Filters**: `SampleCookieGatewayFilterFactory`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

- Follow Java coding conventions
- Use meaningful variable and method names
- Add appropriate comments and documentation
- Ensure all tests pass before submitting

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♂️ Support

For questions and support:

1. Check the [Issues](../../issues) page
2. Review the documentation
3. Contact the development team

## 🔄 Deployment

### Docker Deployment (Future Enhancement)

```yaml
# docker-compose.yml example
version: '3.8'
services:
  eureka-server:
    image: eureka-server:latest
    ports:
      - "8761:8761"
  
  products-service:
    image: msvc-products:latest
    depends_on:
      - eureka-server
      - mysql
  
  gateway-server:
    image: msvc-gateway-server:latest
    ports:
      - "8090:8090"
    depends_on:
      - eureka-server
```

---

**Built with ❤️ using Spring Boot and Spring Cloud**