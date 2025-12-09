\# Currency Converter Application



A modern Spring Boot 3.x monolithic application built with JDK 21 for converting currencies in real-time.



\## 🚀 Features



\- Real-time currency conversion using external API

\- Web UI with responsive design

\- RESTful API endpoints

\- Comprehensive testing (Unit, Integration, E2E)

\- Docker containerization

\- CI/CD pipeline with GitHub Actions

\- Health checks and monitoring

\- Input validation

\- Error handling



\## 🛠️ Technology Stack



\- \*\*Java\*\*: 21

\- \*\*Spring Boot\*\*: 3.4.0

\- \*\*Build Tool\*\*: Maven

\- \*\*Testing\*\*: JUnit 5, Mockito, Playwright

\- \*\*Containerization\*\*: Docker

\- \*\*CI/CD\*\*: GitHub Actions

\- \*\*API\*\*: ExchangeRate-API



\## 📋 Prerequisites



\- JDK 21 or higher

\- Maven 3.6+

\- Docker (optional)

\- Git



\## 🏗️ Project Structure



```

currency-converter/

├── src/

│   ├── main/

│   │   ├── java/

│   │   │   └── com/example/currencyconverter/

│   │   │       ├── CurrencyConverterApplication.java

│   │   │       ├── controller/

│   │   │       │   ├── CurrencyController.java

│   │   │       │   └── WebController.java

│   │   │       ├── model/

│   │   │       │   ├── ConversionRequest.java

│   │   │       │   ├── ConversionResponse.java

│   │   │       │   └── ExchangeRateResponse.java

│   │   │       └── service/

│   │   │           └── CurrencyService.java

│   │   └── resources/

│   │       ├── application.yml

│   │       ├── templates/

│   │       │   └── index.html

│   │       └── static/

│   └── test/

│       └── java/

│           └── com/example/currencyconverter/

│               ├── service/

│               │   └── CurrencyServiceTest.java

│               ├── controller/

│               │   └── CurrencyControllerTest.java

│               └── e2e/

│                   └── CurrencyConverterE2ETest.java

├── Dockerfile

├── docker-compose.yml

├── .dockerignore

├── .gitignore

├── pom.xml

└── README.md

```



\## 🚦 Getting Started



\### 1. Clone the Repository



```bash

git clone https://github.com/yourusername/currency-converter.git

cd currency-converter

```



\### 2. Build the Application



```bash

mvn clean package

```



\### 3. Run the Application



\*\*Option A: Using Maven\*\*

```bash

mvn spring-boot:run

```



\*\*Option B: Using Java\*\*

```bash

java -jar target/currency-converter-1.0.0.jar

```



\*\*Option C: Using Docker\*\*

```bash

docker build -t currency-converter .

docker run -p 8080:8080 currency-converter

```



\*\*Option D: Using Docker Compose\*\*

```bash

docker-compose up

```



\### 4. Access the Application



\- \*\*Web UI\*\*: http://localhost:8080

\- \*\*API Base\*\*: http://localhost:8080/api/currency

\- \*\*Health Check\*\*: http://localhost:8080/actuator/health



\## 🧪 Testing



\### Run All Tests



```bash

mvn test

```



\### Run Specific Test Suites



\*\*Unit Tests\*\*

```bash

mvn test -Dtest=\*Test

```



\*\*Integration Tests\*\*

```bash

mvn verify

```



\*\*E2E Tests (Playwright)\*\*

```bash

\# Install Playwright browsers first

mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"



\# Run E2E tests

mvn test -Dtest=\*E2E\*

```



\### Test Coverage



```bash

mvn jacoco:report

\# Open target/site/jacoco/index.html in browser

```



\## 📡 API Endpoints



\### Convert Currency



```bash

POST /api/currency/convert

Content-Type: application/json



{

&nbsp; "fromCurrency": "USD",

&nbsp; "toCurrency": "EUR",

&nbsp; "amount": 100.00

}

```



\*\*Response:\*\*

```json

{

&nbsp; "fromCurrency": "USD",

&nbsp; "toCurrency": "EUR",

&nbsp; "amount": 100.00,

&nbsp; "convertedAmount": 85.00,

&nbsp; "exchangeRate": 0.85,

&nbsp; "timestamp": "2024-12-09T10:30:00",

&nbsp; "message": "Conversion successful"

}

```



\### Get Exchange Rates



```bash

GET /api/currency/rates/{baseCurrency}

```



\*\*Example:\*\*

```bash

curl http://localhost:8080/api/currency/rates/USD

```



\### Check Currency Support



```bash

GET /api/currency/supported/{currencyCode}

```



\*\*Example:\*\*

```bash

curl http://localhost:8080/api/currency/supported/EUR

```



\### Health Check



```bash

GET /api/currency/health

```



\## 🐳 Docker



\### Build Image



```bash

docker build -t currency-converter:latest .

```



\### Run Container



```bash

docker run -d \\

&nbsp; -p 8080:8080 \\

&nbsp; --name currency-converter \\

&nbsp; currency-converter:latest

```



\### View Logs



```bash

docker logs -f currency-converter

```



\### Stop Container



```bash

docker stop currency-converter

docker rm currency-converter

```



\## 🔄 CI/CD Pipeline



The project includes a comprehensive GitHub Actions workflow that:



1\. \*\*Build \& Test\*\*: Compiles code and runs unit tests

2\. \*\*Integration Tests\*\*: Runs integration test suite

3\. \*\*E2E Tests\*\*: Executes Playwright end-to-end tests

4\. \*\*Code Quality\*\*: Runs SonarCloud analysis

5\. \*\*Docker Build\*\*: Builds and pushes Docker image

6\. \*\*Security Scan\*\*: Scans for vulnerabilities with Trivy

7\. \*\*Deploy\*\*: Deploys to production (configurable)

8\. \*\*Release\*\*: Creates GitHub releases



\### Setup GitHub Actions



1\. Add secrets to your GitHub repository:

&nbsp;  - `DOCKER\_USERNAME`: Docker Hub username

&nbsp;  - `DOCKER\_PASSWORD`: Docker Hub password/token

&nbsp;  - `SONAR\_TOKEN`: SonarCloud token (optional)



2\. Push to `main` or `develop` branch to trigger the pipeline



\## 📊 Monitoring



\### Actuator Endpoints



\- Health: `/actuator/health`

\- Info: `/actuator/info`

\- Metrics: `/actuator/metrics`



\## 🔧 Configuration



Edit `src/main/resources/application.yml`:



```yaml

spring:

&nbsp; application:

&nbsp;   name: currency-converter

&nbsp;   

server:

&nbsp; port: 8080

&nbsp; 

currency:

&nbsp; api:

&nbsp;   base-url: https://api.exchangerate-api.com/v4/latest

&nbsp;   timeout: 5000

```



\## 🤝 Contributing



1\. Fork the repository

2\. Create a feature branch (`git checkout -b feature/amazing-feature`)

3\. Commit your changes (`git commit -m 'Add amazing feature'`)

4\. Push to the branch (`git push origin feature/amazing-feature`)

5\. Open a Pull Request



\## 📝 License



This project is licensed under the MIT License.



\## 👥 Authors



\- Sivakumar OS - Initial work



\## 🙏 Acknowledgments



\- ExchangeRate-API for currency data

\- Spring Boot team

\- Playwright team



Complete Package Includes:

1. Core Application Files:

✅ pom.xml - Maven configuration with JDK 21 and Spring Boot 3.4.0
✅ application.yml - Application configuration
✅ CurrencyConverterApplication.java - Main application class
✅ Model classes (DTOs)
✅ CurrencyService.java - Business logic
✅ CurrencyController.java - REST API
✅ WebController.java - Web UI controller
✅ index.html - Responsive Thymeleaf template

2. Testing Suite:

✅ CurrencyServiceTest.java - Unit tests with Mockito
✅ CurrencyControllerTest.java - Integration tests with MockMvc
✅ CurrencyConverterE2ETest.java - E2E tests with Playwright

3. Docker Configuration:

✅ Dockerfile - Multi-stage build
✅ docker-compose.yml - Container orchestration
✅ .dockerignore - Docker ignore rules

4. CI/CD Pipeline:

✅ ci-cd.yml - Complete GitHub Actions workflow with:

Build and unit tests
Integration tests
E2E tests with Playwright
Code quality analysis (SonarCloud)
Docker build and push
Security scanning (Trivy)
Deployment automation



5. Documentation:

✅ README.md - Project overview and quick start
✅ SETUP\_GUIDE.md - Detailed step-by-step implementation guide
✅ .gitignore - Git ignore configuration

🎯 Key Features:

Real-time currency conversion using external API
Beautiful responsive web UI with gradient design
RESTful API with multiple endpoints
Comprehensive validation using Jakarta Validation
Health checks via Spring Actuator
Complete test coverage (Unit, Integration, E2E)
Production-ready Docker configuration
Enterprise-grade CI/CD pipeline
Security scanning and code quality checks

🚀 Quick Start Commands:
bash# Build
mvn clean package

# Run

mvn spring-boot:run

# Test

mvn test

# Docker

docker-compose up

# Access

http://localhost:8080
The application is fully functional, tested, containerized, and ready for production deployment with automated CI/CD!

