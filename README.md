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

**⚠️ This system is designed to be READ-ONLY and non-destructive by default.**

### Design Philosophy

This AWS monitoring application is built with a **safety-first architecture** to prevent accidental or unauthorized destructive operations on your EC2 infrastructure. Even if termination or modification features are added in the future, they will **NOT execute** unless explicitly enabled via configuration.

### What is Protected?

The safety guard prevents:
- **EC2 instance termination**
- **EC2 instance state modifications** (stop, reboot, etc.)
- **Any destructive AWS operations**

All monitoring and read operations (DescribeInstances, GetMetricStatistics, etc.) work normally.

### How It Works

A centralized `Ec2SafetyGuard` service enforces the safety policy:

```java
@Service
public class Ec2SafetyGuard {
    @Value("${aws.safety.allow-instance-termination:false}")
    private boolean allowInstanceTermination;

    public void checkTerminationAllowed() {
        if (!allowInstanceTermination) {
            throw new TerminationNotAllowedException(
                "EC2 termination is disabled by safety policy"
            );
        }
    }
}
```

Any future code that attempts destructive operations **must** call this guard first:

```java
// Example of how future termination code would be protected:
public void terminateInstance(String instanceId) {
    ec2SafetyGuard.checkTerminationAllowed();  // ← Fails by default
    // ... termination logic ...
}
```

### Configuration

The safety flag is controlled by:

**Option 1: application.properties**
```properties
aws.safety.allow-instance-termination=false  # Default: false
```

**Option 2: Environment Variable**
```bash
export AWS_SAFETY_ALLOW_INSTANCE_TERMINATION=true
```

### Default Behavior

✅ **Default (Production Safe):**
- `ALLOW_INSTANCE_TERMINATION=false`
- System operates in **READ-ONLY mode**
- All monitoring features work normally
- Any destructive operation throws `TerminationNotAllowedException` (HTTP 403)

⚠️ **Enabled (Use with Caution):**
- `ALLOW_INSTANCE_TERMINATION=true`
- Destructive operations are permitted
- **Only enable in controlled environments with proper authorization**

### Why This Matters

1. **Prevents Accidents:** Even if someone adds termination code in the future, it won't execute in production without explicit opt-in
2. **Clear Intent:** The codebase explicitly declares itself as a monitoring tool, not a management tool
3. **Production Safety:** No configuration mistakes or code bugs can accidentally terminate instances
4. **Audit Trail:** Any attempt to perform destructive operations is logged and blocked by default
5. **Future-Proof:** Protects against future feature additions that could be dangerous

### Best Practices

- ❌ **Never enable termination in production** unless you have a specific, documented need
- ✅ **Keep the default (`false`) in all environments**
- ✅ **Review any code that calls `ec2SafetyGuard.checkTerminationAllowed()`**
- ✅ **Treat enabling this flag as a security-sensitive operation**

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

---

## Notes / Limitations

- Max **5,000 samples** per request (server-side guard)
- **IPv4 only**
- Region comes from `.env`
- **READ-ONLY by design** — No destructive EC2 operations are possible without explicit configuration (see Safety Guard section)

---

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
