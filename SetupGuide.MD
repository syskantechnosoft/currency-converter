\# Complete Setup Guide: Spring Boot Currency Converter



\## Step-by-Step Implementation Guide



\### Phase 1: Project Setup (15 minutes)



\#### Step 1.1: Install Prerequisites



\*\*Java 21:\*\*

```bash

\# Check Java version

java -version



\# If not installed, download from:

\# https://adoptium.net/temurin/releases/?version=21

```



\*\*Maven:\*\*

```bash

\# Check Maven version

mvn -version



\# If not installed:

\# Download from https://maven.apache.org/download.cgi

```



\*\*Docker:\*\*

```bash

\# Check Docker version

docker --version



\# If not installed:

\# Download from https://www.docker.com/products/docker-desktop

```



\#### Step 1.2: Create Project Structure



```bash

\# Create project directory

mkdir currency-converter

cd currency-converter



\# Create Maven directory structure

mkdir -p src/main/java/com/example/currencyconverter/{controller,service,model}

mkdir -p src/main/resources/{templates,static}

mkdir -p src/test/java/com/example/currencyconverter/{controller,service,e2e}

mkdir -p .github/workflows

```



\#### Step 1.3: Create Configuration Files



Create the following files in project root:

\- `pom.xml` - Maven configuration

\- `application.yml` - Application properties

\- `Dockerfile` - Docker configuration

\- `docker-compose.yml` - Docker Compose setup

\- `.dockerignore` - Docker ignore rules

\- `.gitignore` - Git ignore rules

\- `README.md` - Project documentation



\### Phase 2: Implement Core Application (30 minutes)



\#### Step 2.1: Main Application Class



Create `src/main/java/com/example/currencyconverter/CurrencyConverterApplication.java`



\#### Step 2.2: Model Classes



Create in `src/main/java/com/example/currencyconverter/model/`:

\- `ConversionRequest.java` - Request DTO

\- `ConversionResponse.java` - Response DTO

\- `ExchangeRateResponse.java` - API response model

\- `CurrencyInfo.java` - Currency information



\#### Step 2.3: Service Layer



Create `src/main/java/com/example/currencyconverter/service/CurrencyService.java`



Key responsibilities:

\- Fetch exchange rates from external API

\- Convert currency amounts

\- Handle API errors

\- Cache results (optional)



\#### Step 2.4: Controller Layer



Create in `src/main/java/com/example/currencyconverter/controller/`:

\- `CurrencyController.java` - REST API endpoints

\- `WebController.java` - Web UI controller



\#### Step 2.5: Web UI Template



Create `src/main/resources/templates/index.html`



Features:

\- Currency selection dropdowns

\- Amount input field

\- Convert button

\- Result display

\- Responsive design



\### Phase 3: Testing Implementation (45 minutes)



\#### Step 3.1: Unit Tests with Mockito



Create `src/test/java/com/example/currencyconverter/service/CurrencyServiceTest.java`



Tests to implement:

```java

\- testConvertCurrency\_Success()

\- testConvertCurrency\_InvalidCurrency()

\- testGetAllRates\_Success()

\- testIsCurrencySupported\_True()

\- testIsCurrencySupported\_False()

```



\#### Step 3.2: Integration Tests



Create `src/test/java/com/example/currencyconverter/controller/CurrencyControllerTest.java`



Tests to implement:

```java

\- testConvertCurrency\_Success()

\- testConvertCurrency\_ValidationError()

\- testGetExchangeRates\_Success()

\- testCheckCurrencySupport()

\- testHealthCheck()

```



\#### Step 3.3: E2E Tests with Playwright



Create `src/test/java/com/example/currencyconverter/e2e/CurrencyConverterE2ETest.java`



Install Playwright browsers:

```bash

mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"

```



Tests to implement:

```java

\- testPageLoads()

\- testFormElementsPresent()

\- testCurrencyConversion()

\- testValidationErrors()

\- testMultipleConversions()

\- testUIResponsiveness()

```



\### Phase 4: Build and Run Locally (15 minutes)



\#### Step 4.1: Build the Project



```bash

\# Clean and build

mvn clean package



\# This will:

\# - Compile source code

\# - Run unit tests

\# - Run integration tests

\# - Create JAR file in target/

```



\#### Step 4.2: Run the Application



\*\*Method 1: Maven\*\*

```bash

mvn spring-boot:run

```



\*\*Method 2: JAR\*\*

```bash

java -jar target/currency-converter-1.0.0.jar

```



\#### Step 4.3: Test the Application



\*\*Open browser:\*\*

```

http://localhost:8080

```



\*\*Test API endpoint:\*\*

```bash

curl -X POST http://localhost:8080/api/currency/convert \\

&nbsp; -H "Content-Type: application/json" \\

&nbsp; -d '{

&nbsp;   "fromCurrency": "USD",

&nbsp;   "toCurrency": "EUR",

&nbsp;   "amount": 100

&nbsp; }'

```



\### Phase 5: Docker Containerization (20 minutes)



\#### Step 5.1: Build Docker Image



```bash

\# Build image

docker build -t currency-converter:1.0 .



\# Verify image

docker images | grep currency-converter

```



\#### Step 5.2: Run Docker Container



```bash

\# Run container

docker run -d \\

&nbsp; --name currency-converter \\

&nbsp; -p 8080:8080 \\

&nbsp; currency-converter:1.0



\# Check logs

docker logs -f currency-converter



\# Check health

docker exec currency-converter wget -qO- http://localhost:8080/actuator/health

```



\#### Step 5.3: Test Containerized Application



```bash

\# Wait for application to start (30 seconds)

sleep 30



\# Test web UI

curl http://localhost:8080



\# Test API

curl -X POST http://localhost:8080/api/currency/convert \\

&nbsp; -H "Content-Type: application/json" \\

&nbsp; -d '{"fromCurrency":"USD","toCurrency":"EUR","amount":100}'

```



\#### Step 5.4: Docker Compose



```bash

\# Start with Docker Compose

docker-compose up -d



\# View logs

docker-compose logs -f



\# Stop services

docker-compose down

```



\### Phase 6: GitHub Actions CI/CD (30 minutes)



\#### Step 6.1: Initialize Git Repository



```bash

\# Initialize Git

git init



\# Add files

git add .



\# Commit

git commit -m "Initial commit: Currency Converter application"



\# Add remote (replace with your repo URL)

git remote add origin https://github.com/yourusername/currency-converter.git



\# Push to GitHub

git push -u origin main

```



\#### Step 6.2: Setup GitHub Secrets



Go to GitHub repository → Settings → Secrets and variables → Actions



Add the following secrets:

\- `DOCKER\_USERNAME`: Your Docker Hub username

\- `DOCKER\_PASSWORD`: Your Docker Hub password or access token

\- `SONAR\_TOKEN`: SonarCloud token (optional)



\#### Step 6.3: Create GitHub Actions Workflow



Create `.github/workflows/ci-cd.yml` with the complete pipeline.



\#### Step 6.4: Trigger Pipeline



```bash

\# Make a change

echo "# Test" >> README.md



\# Commit and push

git add .

git commit -m "Test CI/CD pipeline"

git push origin main

```



Go to GitHub → Actions tab to view pipeline execution.



\### Phase 7: Verification and Testing (20 minutes)



\#### Step 7.1: Run All Tests Locally



```bash

\# Unit tests

mvn test



\# Integration tests

mvn verify



\# E2E tests

mvn test -Dtest=\*E2E\*



\# Generate coverage report

mvn jacoco:report

open target/site/jacoco/index.html

```



\#### Step 7.2: Manual Testing Checklist



\*\*Web UI Testing:\*\*

\- \[ ] Navigate to http://localhost:8080

\- \[ ] Select "From Currency" (e.g., USD)

\- \[ ] Select "To Currency" (e.g., EUR)

\- \[ ] Enter amount (e.g., 100)

\- \[ ] Click "Convert" button

\- \[ ] Verify result displays correctly

\- \[ ] Test with different currency pairs

\- \[ ] Test validation (empty fields, negative amounts)

\- \[ ] Test on mobile viewport



\*\*API Testing:\*\*

\- \[ ] Test `/api/currency/convert` endpoint

\- \[ ] Test `/api/currency/rates/{currency}` endpoint

\- \[ ] Test `/api/currency/supported/{code}` endpoint

\- \[ ] Test `/api/currency/health` endpoint

\- \[ ] Test error scenarios (invalid currency codes)

\- \[ ] Test edge cases (zero amount, very large amounts)



\*\*Docker Testing:\*\*

\- \[ ] Build image successfully

\- \[ ] Container starts without errors

\- \[ ] Health check passes

\- \[ ] Application accessible on port 8080

\- \[ ] Logs show no errors

\- \[ ] Can stop and restart container



\*\*CI/CD Testing:\*\*

\- \[ ] Pipeline triggers on push

\- \[ ] All jobs complete successfully

\- \[ ] Tests pass in CI environment

\- \[ ] Docker image builds and pushes

\- \[ ] Security scan completes

\- \[ ] Artifacts uploaded



\### Phase 8: Deployment Options (Optional)



\#### Option 1: Deploy to Heroku



```bash

\# Install Heroku CLI

\# https://devcenter.heroku.com/articles/heroku-cli



\# Login to Heroku

heroku login



\# Create app

heroku create currency-converter-app



\# Deploy

git push heroku main



\# Open app

heroku open

```



\#### Option 2: Deploy to AWS



```bash

\# Install AWS CLI

\# https://aws.amazon.com/cli/



\# Configure AWS credentials

aws configure



\# Push to ECR

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR\_ECR\_URL

docker tag currency-converter:latest YOUR\_ECR\_URL/currency-converter:latest

docker push YOUR\_ECR\_URL/currency-converter:latest



\# Deploy to ECS (create task definition and service)

```



\#### Option 3: Deploy to Kubernetes



```bash

\# Create deployment.yaml

kubectl apply -f k8s/deployment.yaml



\# Create service.yaml

kubectl apply -f k8s/service.yaml



\# Expose service

kubectl expose deployment currency-converter --type=LoadBalancer --port=8080

```



\### Troubleshooting



\#### Common Issues and Solutions



\*\*Issue: Port 8080 already in use\*\*

```bash

\# Find process using port 8080

lsof -i :8080  # Mac/Linux

netstat -ano | findstr :8080  # Windows



\# Kill process or change port in application.yml

server:

&nbsp; port: 8081

```



\*\*Issue: Tests failing with timeout\*\*

```bash

\# Increase timeout in application.yml

currency:

&nbsp; api:

&nbsp;   timeout: 10000  # 10 seconds

```



\*\*Issue: Docker build fails\*\*

```bash

\# Clear Docker cache

docker system prune -a



\# Rebuild without cache

docker build --no-cache -t currency-converter .

```



\*\*Issue: Playwright browsers not installed\*\*

```bash

\# Install browsers

mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"

```



\*\*Issue: Maven build fails\*\*

```bash

\# Clean Maven cache

mvn dependency:purge-local-repository



\# Force update dependencies

mvn clean install -U

```



\### Performance Optimization Tips



1\. \*\*Enable caching for exchange rates\*\*

```java

@Cacheable("exchangeRates")

public Map<String, BigDecimal> getAllRates(String baseCurrency) {

&nbsp;   // Implementation

}

```



2\. \*\*Configure connection pooling\*\*

```yaml

spring:

&nbsp; webflux:

&nbsp;   client:

&nbsp;     max-connections: 100

&nbsp;     pending-acquire-timeout: 5000

```



3\. \*\*Optimize Docker image\*\*

```dockerfile

\# Use slim JRE instead of JDK

FROM eclipse-temurin:21-jre-alpine

```



4\. \*\*Enable compression\*\*

```yaml

server:

&nbsp; compression:

&nbsp;   enabled: true

&nbsp;   mime-types: text/html,text/xml,text/plain,application/json

```



\### Next Steps



1\. \*\*Add more features:\*\*

&nbsp;  - Historical exchange rates

&nbsp;  - Multiple currency conversions

&nbsp;  - Favorite currency pairs

&nbsp;  - Conversion history



2\. \*\*Enhance security:\*\*

&nbsp;  - Add API authentication

&nbsp;  - Rate limiting

&nbsp;  - CORS configuration

&nbsp;  - HTTPS enforcement



3\. \*\*Improve monitoring:\*\*

&nbsp;  - Add Prometheus metrics

&nbsp;  - Grafana dashboards

&nbsp;  - Log aggregation (ELK stack)

&nbsp;  - APM tools (New Relic, Datadog)



4\. \*\*Scale the application:\*\*

&nbsp;  - Kubernetes deployment

&nbsp;  - Load balancing

&nbsp;  - Database integration

&nbsp;  - Redis caching



\### Resources



\- \*\*Spring Boot Documentation\*\*: https://spring.io/projects/spring-boot

\- \*\*Docker Documentation\*\*: https://docs.docker.com

\- \*\*GitHub Actions\*\*: https://docs.github.com/actions

\- \*\*Playwright\*\*: https://playwright.dev/java/

\- \*\*JUnit 5\*\*: https://junit.org/junit5/

\- \*\*Mockito\*\*: https://site.mockito.org



\### Conclusion



You now have a complete, production-ready Spring Boot application with:

\- ✅ Modern Java 21 features

\- ✅ Comprehensive testing (Unit, Integration, E2E)

\- ✅ Docker containerization

\- ✅ CI/CD pipeline with GitHub Actions

\- ✅ REST API and Web UI

\- ✅ Monitoring and health checks

\- ✅ Best practices and code quality



Happy coding! 🚀

