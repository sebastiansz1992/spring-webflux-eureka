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
- [Monitoring & Observability](#monitoring--observability)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)

## 🎯 Overview

This project demonstrates a modern microservices architecture built with Spring Boot, featuring:

- **Service Discovery** with Netflix Eureka
- **Centralized Configuration** with Spring Cloud Config
- **API Gateway** with Spring Cloud Gateway
- **Distributed Tracing** with Micrometer and Zipkin
- **Reactive Programming** with Spring WebFlux
- **Load Balancing** with Spring Cloud LoadBalancer
- **Inter-service Communication** using Feign and WebClient
- **Database Integration** with JPA and MySQL
- **Observability** with Spring Boot Actuator and Micrometer

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
   │   (Port: Random)    │      │       │   (Port: 8005)    │
   │   - JPA/MySQL       │      │       │   - WebClient     │
   │   - CRUD Operations │      │       │   - Feign Client  │
   │   - Config Client   │      │       │   - Config Client │
   └──────────┬──────────┘      │       └─────────┬─────────┘
              │                 │                 │
              │     ┌───────────▼───────────┐     │
              │     │   Eureka Server       │     │
              │     │   (Port: 8761)       │     │
              │     │   - Service Registry  │     │
              │     │   - Health Monitoring │     │
              │     └───────────────────────┘     │
              │                                   │
              └─────────────┬─────────────────────┘
                            │
                ┌───────────▼───────────┐
                │   Config Server       │
                │   (Port: 8888)       │
                │   - Centralized Config│
                │   - Git Repository   │
                │   - Profile Management│
                └───────────────────────┘

                    Observability Layer
        ┌──────────────────────────────────────┐
        │                                      │
   ┌────▼─────┐                         ┌─────▼────┐
   │  Zipkin  │                         │ Actuator │
   │(Port:9411)│◄────Traces─────────────│ Metrics  │
   │ Tracing  │                         │  & Info  │
   └──────────┘                         └──────────┘
```

## 🚀 Services

### 1. Eureka Server (`eureka-server`)
- **Port**: 8761
- **Purpose**: Service registry and discovery
- **Technology**: Spring Cloud Netflix Eureka Server
- **URL**: http://localhost:8761

### 2. Config Server (`config-server`)
- **Port**: 8888
- **Purpose**: Centralized configuration management
- **Technology**: Spring Cloud Config Server
- **URL**: http://localhost:8888
- **Features**:
  - Git-based configuration repository
  - Environment-specific profiles (dev, prod)
  - Real-time configuration updates
  - Encrypted property support

### 3. Gateway Server (`msvc-gateway-server`)
- **Port**: 8090
- **Purpose**: API Gateway with routing and load balancing
- **Technology**: Spring Cloud Gateway WebFlux
- **Features**:
  - Dynamic routing to microservices
  - Global and custom filters
  - Request/Response transformation
  - Load balancing

### 4. Products Service (`msvc-products`)
- **Port**: Random (auto-assigned)
- **Purpose**: Product catalog management
- **Technology**: Spring Boot Web + JPA
- **Database**: MySQL (`db_springboot_cloud`)
- **Features**:
  - CRUD operations for products
  - Database persistence
  - Eureka client registration

### 5. Items Service (`msvc-items`)
- **Port**: 8005 (configurable via Config Server)
- **Purpose**: Business logic aggregator
- **Technology**: Spring Boot WebFlux + Feign
- **Features**:
  - Consumes Products Service
  - Reactive programming
  - Feign and WebClient implementations
  - Load balancing
  - Externalized configuration with Spring Cloud Config
  - Multiple environment profiles (dev, prod)

## 🌐 Quick Access URLs

Once all services are running, access them at:

| Service | URL | Description |
|---------|-----|-------------|
| **Eureka Dashboard** | http://localhost:8761 | Service registry and discovery |
| **Zipkin UI** | http://localhost:9411 | Distributed tracing visualization |
| **Config Server** | http://localhost:8888 | Configuration management |
| **Gateway API** | http://localhost:8090 | API Gateway entry point |
| **Items Actuator** | http://localhost:8005/actuator | Metrics and health endpoints |
| **Gateway Actuator** | http://localhost:8090/actuator | Gateway metrics and health |

### API Endpoints via Gateway

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/products/` | GET | List all products |
| `/api/products/{id}` | GET | Get product by ID |
| `/api/items/` | GET | List all items |
| `/api/items/{id}` | GET | Get item by ID |

## 🛠️ Technologies

| Component | Technology | Version |
|-----------|------------|---------|
| **Framework** | Spring Boot | 3.5.6 |
| **Cloud** | Spring Cloud | 2025.0.0 |
| **Language** | Java | 21 |
| **Build Tool** | Maven | 3.9.11 |
| **Database** | MySQL | Latest |
| **Service Discovery** | Netflix Eureka | Latest |
| **Configuration** | Spring Cloud Config | Latest |
| **Gateway** | Spring Cloud Gateway | Latest |
| **Distributed Tracing** | Micrometer + Zipkin | Latest |
| **Observability** | Spring Boot Actuator | Latest |
| **Reactive** | Spring WebFlux | Latest |
| **HTTP Client** | OpenFeign | Latest |

## 📋 Prerequisites

Before running this project, ensure you have:

- **Java 21** or higher
- **Maven 3.9+** (or use included Maven wrapper)
- **MySQL Server** running on port 3306
- **Zipkin Server** (optional, for distributed tracing)
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

### Zipkin Setup (Optional - for Distributed Tracing)

**Option 1: Using Docker (Recommended)**
```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

**Option 2: Download JAR**
```bash
# Download Zipkin
curl -sSL https://zipkin.io/quickstart.sh | bash -s

# Run Zipkin
java -jar zipkin.jar
```

**Option 3: Using PowerShell**
```powershell
# Download Zipkin
Invoke-WebRequest -Uri https://search.maven.org/remote_content?g=io.zipkin&a=zipkin-server&v=LATEST&c=exec -OutFile zipkin.jar

# Run Zipkin
java -jar zipkin.jar
```

Access Zipkin UI at: http://localhost:9411

## 🚀 Getting Started

### Option 1: Using Maven Wrapper (Recommended)

1. **Clone the repository**
```bash
git clone https://github.com/sebastiansz1992/spring-webflux-eureka.git
cd spring-webflux-eureka
```

2. **Start services in order**

> **Note**: Use `.\mvnw` on Windows PowerShell or `./mvnw` on Unix/Mac

**Step 1: Start Config Server**
```bash
# Linux/Mac
cd config-server
./mvnw spring-boot:run

# Windows PowerShell
cd config-server
.\mvnw spring-boot:run
# Wait for startup (check http://localhost:8888)
```

**Step 2: Start Eureka Server**
```bash
# Linux/Mac
cd ../eureka-server
./mvnw spring-boot:run

# Windows PowerShell
cd ..\eureka-server
.\mvnw spring-boot:run
# Wait for startup (check http://localhost:8761)
```

**Step 3: Start Products Service**
```bash
# Linux/Mac
cd ../msvc-products
./mvnw spring-boot:run

# Windows PowerShell
cd ..\msvc-products
.\mvnw spring-boot:run
# Multiple instances can be started on different ports
```

**Step 4: Start Items Service**
```bash
# Linux/Mac
cd ../msvc-items
./mvnw spring-boot:run

# Windows PowerShell
cd ..\msvc-items
.\mvnw spring-boot:run
```

**Step 5: Start Gateway Server**
```bash
# Linux/Mac
cd ../msvc-gateway-server
./mvnw spring-boot:run

# Windows PowerShell
cd ..\msvc-gateway-server
.\mvnw spring-boot:run
```

### Option 2: Using IDE

1. Import the project as a Maven multi-module project
2. Start each service individually through your IDE's run configuration

### Option 3: Using JAR files

```bash
# Build all services
mvn clean package -DskipTests

# Run each service (in order)
java -jar config-server/target/config-server-0.0.1-SNAPSHOT.jar
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
| Config Server | 8888 | Fixed |
| Gateway Server | 8090 | Fixed |
| Products Service | Random | `PORT` (default: random) |
| Items Service | 8005 | `server.port` (via Config Server) |

### Environment Variables

#### Products Service
```properties
PORT=0                          # Random port assignment
SPRING_DATASOURCE_URL          # MySQL connection URL
SPRING_DATASOURCE_USERNAME     # Database username
SPRING_DATASOURCE_PASSWORD     # Database password
```

#### Config Server
```properties
spring.cloud.config.server.git.uri         # Git repository URL
spring.cloud.config.server.git.searchPaths # Config files path
spring.cloud.config.server.git.default-label # Default branch
```

#### Items Service (with Config Client)
```properties
spring.application.name=msvc-items         # Service name for config
spring.cloud.config.uri=http://localhost:8888 # Config Server URL
spring.profiles.active=dev                # Active profile
```

#### Distributed Tracing Configuration
```properties
# Micrometer Tracing
management.tracing.sampling.probability=1.0    # Sample 100% of requests (use 0.1 for 10% in production)
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans

# Actuator Endpoints
management.endpoints.web.exposure.include=*    # Expose all actuator endpoints
management.endpoint.health.show-details=always # Show detailed health information

# Metrics Export
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.tags.application=${spring.application.name}
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

#### Config Server Endpoints
```
GET    http://localhost:8888/{application}/{profile}           # Get config for app/profile
GET    http://localhost:8888/{application}/{profile}/{label}   # Get config with branch/label
GET    http://localhost:8888/msvc-items/dev                    # Items service dev config
GET    http://localhost:8888/msvc-items/prod                   # Items service prod config
```

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
GET    http://localhost:8005/              # List items (dev profile)
GET    http://localhost:8005/{id}          # Get item
GET    http://localhost:8007/              # List items (prod profile)
GET    http://localhost:8007/{id}          # Get item
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
2. Test Config Server:
```bash
curl http://localhost:8888/msvc-items/dev
curl http://localhost:8888/msvc-items/prod
```

3. Test service discovery:
```bash
curl http://localhost:8761/eureka/apps
```

4. Test gateway routing:
```bash
curl http://localhost:8090/api/products/
curl http://localhost:8090/api/items/
```

5. Test distributed tracing:
```bash
# Make several requests through the gateway
curl http://localhost:8090/api/items/1
curl http://localhost:8090/api/items/2
curl http://localhost:8090/api/products/

# View traces in Zipkin UI
# Open http://localhost:9411 and click "Run Query"
```

### Distributed Tracing Testing

**Testing Trace Propagation:**
1. Ensure Zipkin is running on http://localhost:9411
2. Make a request through the Gateway:
```bash
curl http://localhost:8090/api/items/1
```
3. Check the response headers for trace ID:
```bash
curl -v http://localhost:8090/api/items/1 2>&1 | grep -i trace
```
4. Open Zipkin UI and search for the trace
5. Verify all services appear in the trace:
   - Gateway Server
   - Items Service
   - Products Service

**Expected Trace Flow:**
```
Gateway (msvc-gateway-server)
  └── Items Service (msvc-items)
      └── Products Service (msvc-products)
          └── MySQL Database
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

## 📊 Monitoring & Observability

### Distributed Tracing with Zipkin

**Zipkin Dashboard**
- **URL**: http://localhost:9411
- **Purpose**: Visualize distributed traces across microservices
- **Features**:
  - End-to-end request tracing
  - Service dependency graph
  - Latency analysis
  - Performance bottleneck identification

**How it works:**
1. Micrometer automatically instruments HTTP requests
2. Trace IDs are propagated across service boundaries
3. Spans are collected and sent to Zipkin
4. Visualize complete request flows in Zipkin UI

**Viewing Traces:**
1. Start all services and Zipkin
2. Make requests through the Gateway
3. Open http://localhost:9411
4. Click "Run Query" to see recent traces
5. Click on a trace to see detailed span information

### Micrometer Metrics

Each service exposes metrics through Spring Boot Actuator:

**Available Metrics Endpoints:**
```bash
# View all available metrics
curl http://localhost:8005/actuator/metrics

# Specific metrics examples
curl http://localhost:8005/actuator/metrics/http.server.requests
curl http://localhost:8005/actuator/metrics/jvm.memory.used
curl http://localhost:8005/actuator/metrics/system.cpu.usage
curl http://localhost:8005/actuator/metrics/process.uptime
```

**Common Metrics Categories:**
- **HTTP Metrics**: Request counts, response times, status codes
- **JVM Metrics**: Memory usage, garbage collection, thread counts
- **System Metrics**: CPU usage, file descriptors, disk space
- **Custom Business Metrics**: Application-specific measurements

### Eureka Dashboard
- **URL**: http://localhost:8761
- **Purpose**: View registered services and health status
- **Features**:
  - Service registry overview
  - Instance health status
  - Service metadata
  - Lease information

### Application Logs

Each service provides detailed logging for:
- Service registration/deregistration
- HTTP requests and responses
- Distributed trace IDs (for correlation)
- Load balancer decisions
- Filter executions
- Configuration changes

**Log Correlation with Trace IDs:**
```
INFO [msvc-items,64f0c3e8c7b4a123,64f0c3e8c7b4a123] - Processing request
```
Format: `[application-name,trace-id,span-id]`

### Health Checks
```bash
# Check individual service health
curl http://localhost:8888/actuator/health  # Config Server
curl http://localhost:8005/actuator/health  # Items Service
curl http://localhost:8090/actuator/health  # Gateway Server

# Detailed health information (if enabled)
curl http://localhost:8005/actuator/health/liveness
curl http://localhost:8005/actuator/health/readiness
```

### Actuator Endpoints

**Common Actuator Endpoints:**
```bash
# Info endpoint
curl http://localhost:8005/actuator/info

# Environment variables
curl http://localhost:8005/actuator/env

# Configuration properties
curl http://localhost:8005/actuator/configprops

# Beans in application context
curl http://localhost:8005/actuator/beans

# Thread dump
curl http://localhost:8005/actuator/threaddump

# Heap dump (download)
curl http://localhost:8005/actuator/heapdump -O

# HTTP traces
curl http://localhost:8005/actuator/httptrace
```

## 🔧 Development

### Configuration Management

The project uses **Spring Cloud Config** for externalized configuration:

#### Available Profiles:
- **Default**: Basic configuration (`config/msvc-items.properties`)
- **Development**: Dev-specific settings (`config/msvc-items-dev.properties`)
- **Production**: Prod-specific settings (`config/msvc-items-prod.properties`)

#### Configuration Files Location:
- **Repository**: https://github.com/sebastiansz1992/spring-webflux-eureka
- **Path**: `/config` directory
- **Format**: `{service-name}-{profile}.properties`

#### Configuration Examples:

**Default (`msvc-items.properties`)**:
```properties
server.port=8005
configuracion.texto=Configurando entorno Desarrollo
```

**Development (`msvc-items-dev.properties`)**:
```properties
configuracion.texto=Configurando ambiente de Desarrollo
configuracion.autor.nombre=Sebastian Agudelo
configuracion.autor.email=sebascarman@gmail.com
```

**Production (`msvc-items-prod.properties`)**:
```properties
configuracion.texto=Configurando ambiente de Produccion
server.port=8007
configuracion.autor.nombre=Sebastian Agudelo
configuracion.autor.email=sebascarman@gmail.com
```

#### Switching Profiles:
Update `bootstrap.properties` in the service:
```properties
spring.profiles.active=prod  # Changes to production profile
```

### Enabling Distributed Tracing

To add Micrometer and Zipkin tracing to a service, add these dependencies to `pom.xml`:

```xml
<dependencies>
    <!-- Micrometer Tracing Bridge for Brave -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-tracing-bridge-brave</artifactId>
    </dependency>
    
    <!-- Zipkin Reporter -->
    <dependency>
        <groupId>io.zipkin.reporter2</groupId>
        <artifactId>zipkin-reporter-brave</artifactId>
    </dependency>
    
    <!-- Spring Boot Actuator -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

Then configure in `application.properties`:
```properties
# Enable tracing
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans

# Expose actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```

### Adding a New Service

1. Create a new Spring Boot project
2. Add dependencies:
   - Eureka Client
   - Config Client (for externalized config)
   - Micrometer Tracing (for distributed tracing)
   - Spring Boot Actuator (for metrics)
3. Configure `bootstrap.properties`:
```properties
spring.application.name=your-service-name
spring.cloud.config.uri=http://localhost:8888
spring.profiles.active=dev
```
4. Configure `application.properties`:
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```
5. Add configuration files to the `/config` directory in the Git repository
6. Add gateway routing in `msvc-gateway-server/application.yml`

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

## 🔧 Troubleshooting

### Zipkin Connection Issues

**Problem**: Services can't connect to Zipkin
```
Unable to send spans to Zipkin: Connection refused
```

**Solutions**:
1. Verify Zipkin is running:
```bash
curl http://localhost:9411/health
```

2. Check Zipkin endpoint in configuration:
```properties
management.zipkin.tracing.endpoint=http://localhost:9411/api/v2/spans
```

3. If Zipkin is down, services will continue to work but traces won't be collected

### No Traces Appearing in Zipkin

**Problem**: Traces not showing in Zipkin UI

**Solutions**:
1. Check sampling probability (should be 1.0 for testing):
```properties
management.tracing.sampling.probability=1.0
```

2. Verify Micrometer dependencies are included in `pom.xml`

3. Check service logs for trace IDs:
```
[msvc-items,64f0c3e8c7b4a123,64f0c3e8c7b4a123]
```

4. Make requests and wait a few seconds for spans to be sent

### Actuator Endpoints Not Accessible

**Problem**: 404 on `/actuator/*` endpoints

**Solutions**:
1. Ensure actuator dependency is included
2. Check endpoint exposure:
```properties
management.endpoints.web.exposure.include=*
```

3. Verify the service is running on the expected port

## 🔄 Deployment

### Docker Deployment with Observability Stack

```yaml
# docker-compose.yml
version: '3.8'
services:
  zipkin:
    image: openzipkin/zipkin
    container_name: zipkin
    ports:
      - "9411:9411"
    networks:
      - microservices-network

  mysql:
    image: mysql:8
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: admin
      MYSQL_DATABASE: db_springboot_cloud
    ports:
      - "3306:3306"
    networks:
      - microservices-network

  config-server:
    image: config-server:latest
    container_name: config-server
    ports:
      - "8888:8888"
    environment:
      SPRING_PROFILES_ACTIVE: docker
    networks:
      - microservices-network

  eureka-server:
    image: eureka-server:latest
    container_name: eureka-server
    ports:
      - "8761:8761"
    depends_on:
      - config-server
    environment:
      SPRING_CLOUD_CONFIG_URI: http://config-server:8888
      MANAGEMENT_ZIPKIN_TRACING_ENDPOINT: http://zipkin:9411/api/v2/spans
    networks:
      - microservices-network
  
  products-service:
    image: msvc-products:latest
    container_name: products-service
    depends_on:
      - eureka-server
      - mysql
      - zipkin
    environment:
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/db_springboot_cloud
      MANAGEMENT_ZIPKIN_TRACING_ENDPOINT: http://zipkin:9411/api/v2/spans
    networks:
      - microservices-network

  items-service:
    image: msvc-items:latest
    container_name: items-service
    depends_on:
      - eureka-server
      - config-server
      - zipkin
    environment:
      SPRING_CLOUD_CONFIG_URI: http://config-server:8888
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
      MANAGEMENT_ZIPKIN_TRACING_ENDPOINT: http://zipkin:9411/api/v2/spans
    networks:
      - microservices-network
  
  gateway-server:
    image: msvc-gateway-server:latest
    container_name: gateway-server
    ports:
      - "8090:8090"
    depends_on:
      - eureka-server
      - zipkin
    environment:
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
      MANAGEMENT_ZIPKIN_TRACING_ENDPOINT: http://zipkin:9411/api/v2/spans
    networks:
      - microservices-network

networks:
  microservices-network:
    driver: bridge
```

### Deployment Commands

```bash
# Build all services
mvn clean package -DskipTests

# Build Docker images
docker build -t config-server:latest ./config-server
docker build -t eureka-server:latest ./eureka-server
docker build -t msvc-products:latest ./msvc-products
docker build -t msvc-items:latest ./msvc-items
docker build -t msvc-gateway-server:latest ./msvc-gateway-server

# Start all services with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Accessing Services in Docker

- **Zipkin UI**: http://localhost:9411
- **Eureka Dashboard**: http://localhost:8761
- **Gateway**: http://localhost:8090
- **Config Server**: http://localhost:8888

---

**Built with ❤️ using Spring Boot, Spring Cloud, and Observability Best Practices**