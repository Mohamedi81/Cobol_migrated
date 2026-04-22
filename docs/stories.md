# User Stories – COBOL to Java Microservices Migration

This document captures the functional behavior of the original CICS/COBOL application (`cics-genapp`) in the form of user stories, to drive the Java microservices design and implementation.

---

## 1. Policy Management

1. **Create policy**

As a customer service agent, I want to create a new policy for an existing customer by entering customer ID, product, coverage, effective dates and premium, so that the customer is covered under the requested product.

2. **Retrieve policy by number**

As a customer service agent, I want to retrieve a policy by policy number so that I can view policy details and respond to customer queries.

3. **Retrieve policies by customer**

As a customer service agent, I want to retrieve all policies for a given customer so that I can see a complete view of customer coverage.

4. **Update policy details**

As a customer service agent, I want to update selected policy attributes (e.g., address, contact, coverage options, payment details) so that policy information stays accurate.

5. **Cancel policy**

As a customer service agent, I want to cancel a policy and set its status and end date appropriately so that it no longer provides active coverage.

6. **Validate policy data**

As a customer service agent, I want the system to validate mandatory policy fields (e.g., customer exists, dates are valid, premium non-negative) and return user-friendly error codes so that I avoid corrupt data.

7. **Search policies**

As a customer service agent, I want to search policies by various criteria (policy number prefix, customer ID, product code) so that I can quickly locate the right record.

---

## 2. Customer Management

8. **Create customer**

As a customer onboarding agent, I want to create a new customer record with identity and contact details so that I can attach policies to the customer.

9. **Retrieve customer by ID**

As a customer service agent, I want to retrieve a customer by customer ID so that I can verify identity and details.

10. **Update customer details**

As a customer service agent, I want to update customer address and contact information so that correspondence goes to the right place.

11. **Validate customer data**

As a customer service agent, I want the system to validate mandatory customer fields (name, address, contact) and return meaningful error codes so that I can correct input.

---

## 3. Statistics & Reporting

12. **View policy statistics**

As a supervisor, I want to see counts of policies by product, status, and region so that I can understand portfolio distribution.

13. **View premium totals**

As a supervisor, I want to see total premiums by product and region so that I can track revenue.

14. **Export statistics**

As a reporting user, I want to retrieve statistics in a machine-readable format (JSON/CSV) so that I can consume them in external reporting tools.

---

## 4. Setup / Test Data Management

15. **Initial data load**

As a system administrator, I want to bulk-load base reference and test data (customers, policies) into the system so that developers and testers have a pre-populated environment.  
(COBOL reference: `lgsetup.cbl`.)

16. **Test scenarios**

As a tester, I want to run predefined functional tests by calling the back-end routines with sample data and capturing outputs so that I can verify behavior after changes.  
(COBOL reference: `lgtestc1`, `lgtestp1–p4`.)

---

## 5. Integration / Web API

17. **Web/screen front-end integration**

As a web front-end, I want to call back-end policy and customer services via a well-defined API schema, with request and response payloads matching the COBOL commarea semantics, so that the UI behaves like the CICS screen applications.  
(COBOL reference: `lgwebst5.cbl`, `ssmap.bms`, `soa*` copybooks.)

18. **Error and status handling**

As any caller (UI, batch, external system), I want consistent error codes and messages returned for validation failures, missing records, or system errors so that I can handle them gracefully.
