# AWS Monitor — Home Assignment

Full-stack application that resolves an AWS EC2 instance by IP address and displays **CPU Utilization (%) over time** using **CloudWatch** metrics.

---

## Tech Stack

- **Backend:** Java 17, Spring Boot, AWS SDK v2 (EC2 + CloudWatch)
- **Frontend:** Angular 21, TypeScript, Chart.js (ng2-charts)

---

## How It Works

1. User enters:
   - EC2 instance IP
   - Time range
   - Sampling interval
2. Frontend sends a request to:
   - `POST /api/cpu/usage`
3. Backend:
   - Validates the IP
   - Resolves `IP → InstanceId` via EC2 `DescribeInstances`
   - Fetches CPU metrics via CloudWatch `GetMetricStatistics`
   - Returns JSON response
4. Frontend renders the response as a line chart

---

## Safety Guard

This system is **READ-ONLY by default** and does not allow destructive EC2 operations.

A backend safety flag prevents EC2 termination or modification unless explicitly enabled:

```properties
aws.safety.allow-instance-termination=false
```

---

## Prerequisites

- Java 17+
- Maven 3.6+
- Node.js 18+ + npm
- AWS credentials with required permissions

---


## Configuration (AWS Credentials)

Create `.env` from the example:

```bash
cp .env.example .env
```

Fill in:

```env
AWS_ACCESS_KEY_ID=...
AWS_SECRET_ACCESS_KEY=...
AWS_REGION=us-east-1
```


## Run the Backend

```bash
cd aws-monitor-backend
./mvnw spring-boot:run
```

Backend URL: `http://localhost:8080`

---

## Run the Frontend

```bash
cd aws-monitor-frontend
npm install
npm start
```

Frontend URL: `http://localhost:4200`

Development proxy forwards:

- `/api/* → http://localhost:8080`

---

## API

### Request

`POST /api/cpu/usage`

```json
{
  "ip": "YOUR_EC2_PRIVATE_OR_PUBLIC_IP",
  "minutesBack": 60,
  "intervalSeconds": 300
}
```

### Response

```json
{
  "instanceId": "i-xxxxxxxxx",
  "points": [
    { "timestamp": "2026-02-08T10:00:00Z", "value": 45.2 }
  ]
}
```

## Resources

1. Exception Handling Documentation:  
   https://medium.com/@sharmapraveen91/handle-exceptions-in-spring-boot-a-guide-to-clean-code-principles-e8a9d56cafe8

2. IP Address Validation:  
   https://www.regular-expressions.info/ip.html

3. AWS SDK for Java v2:  
   https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html

4. Angular Proxy Configuration:  
   https://angular.dev/tools/cli/serve#proxying-to-a-backend-server

5. Chart.js:  
   https://www.chartjs.org
