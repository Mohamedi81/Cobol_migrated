# COBOL GenApp Migration – Java Microservices

This repo contains a Java microservices migration of the IBM CICS sample application
[`cicsdev/cics-genapp`](https://github.com/cicsdev/cics-genapp).

## Original Domain

The COBOL/CICS application manages insurance **customers** and **policies** using CICS, DB2 and VSAM.
Key COBOL modules:

- Customer: `lgacdb01.cbl`, `lgucdb01.cbl`, `lgucus01.cbl` etc.
- Policy & versions: `lgapdb01.cbl`, `lgipdb01.cbl`, `lgupdb01.cbl`, `lgapvs01.cbl` etc.
- Front-end and setup: `lgwebst5.cbl`, `lgsetup.cbl`.

## Target Architecture

The migration uses Spring Boot microservices:

- `customer-service` – CRUD and status management for customers.
- `policy-service` – policy creation, updates, cancellation and version history.
- `gateway-service` – unified REST API and basic auth/roles.

All services are packaged as separate Spring Boot apps and share a PostgreSQL database for this sample.

## User Stories (from COBOL analysis)

Highlights:

- CUST-1..4: Create, read, update, deactivate customers.
- POL-1..4: Create policy, retrieve by number, update details, cancel/terminate.
- PV-1..2: Maintain policy version history and as-of date queries.
- ADM-1..2: Initialize data and expose health/statistics (via actuator here).
- WEB-1..2: Unified REST API and role-based access (`ROLE_CLERK`, `ROLE_UNDERWRITER`, `ROLE_ADMIN`).

## Running Locally

1. Start the database:

   ```bash
   docker-compose up -d
   ```

2. Build all services:

   ```bash
   mvn clean package
   ```

3. Run services (separate terminals):

   ```bash
   cd customer-service && mvn spring-boot:run
   cd policy-service && mvn spring-boot:run
   cd gateway-service && mvn spring-boot:run
   ```

4. Test endpoints via the gateway (basic auth):

   - Customer creation:

     ```bash
     curl -u clerk:clerk -X POST http://localhost:8080/api/customers \
       -H "Content-Type: application/json" \
       -d '{
         "customerCode": "C123",
         "firstName": "John",
         "lastName": "Doe"
       }'
     ```

   - Policy creation:

     ```bash
     curl -u uw:uw -X POST http://localhost:8080/api/policies \
       -H "Content-Type: application/json" \
       -d '{
         "policyNumber": "P100",
         "customerId": 1,
         "productCode": "HOME",
         "effectiveDate": "2025-01-01",
         "premiumAmount": 1000.00,
         "coverageAmount": 250000.00
       }'
     ```

## Mapping COBOL → Microservices

- `lgacdb01.cbl`, `lgucdb01.cbl`, `lgucus01.cbl` → `customer-service` domain, repository and controller.
- `lgapdb01.cbl`, `lgipdb01.cbl`, `lgupdb01.cbl`, `lgapvs01.cbl` → `policy-service` domain, versioning rules.
- `lgwebst5.cbl` → `gateway-service` routing and orchestration.
- `lgsetup.cbl`, `lgastat1.cbl` → handled via Spring Boot actuator and could be extended with data seeding.

## Next Steps

- Replace in-memory security in the gateway with OAuth2/JWT.
- Add Flyway or Liquibase migrations instead of `ddl-auto=update`.
- Implement integration tests mirroring COBOL test harness programs (`lgtest*`).
