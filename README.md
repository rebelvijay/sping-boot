# Spring Boot Demo Project 🚀

This is a simple Spring Boot application integrated with CI/CD pipeline, unit testing, and code coverage (JaCoCo).

---

## 📌 Tech Stack

- Java 21
- Spring Boot
- Maven
- JUnit 5
- JaCoCo
- Jenkins (CI/CD ready)

---

## 🚀 Run Project

### 1️⃣ Build Project
```bash
mvn clean install
mvn clean test jacoco:report
mvn spring-boot:run
mvn package -DskipTests
java -jar target/springboot-demo-1.0.0.jar
http://localhost:8081
target/site/jacoco/index.html
mvn clean package -DskipTests
docker build -t springboot-demo .
docker run -p 8081:8081 springboot-demo