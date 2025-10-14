# Discount Coupon Feature Requirements

## Business Requirements

1. **Offer Promotional Discounts**
   - As a marketing manager, I want to issue coupon codes that apply promotional discounts to customer orders so that we can run campaigns that increase conversions and average order value.
2. **Support Multiple Coupon Types**
   - The system must allow both percentage-based and fixed-amount discounts to accommodate different campaign strategies.
3. **Targeted Eligibility**
   - Coupons should be configurable with eligibility rules (e.g., minimum order subtotal, specific customer segments, product/category inclusions or exclusions) to focus promotions on the intended audience.
4. **Transparent Checkout Experience**
   - Customers must see applied coupon details, including discount amount and any validation errors, directly in the checkout UI for clarity and trust.
5. **Operational Oversight**
   - Administrators require visibility into coupon usage metrics (redemptions, remaining quota, failures) to monitor campaign performance and adjust strategy.
6. **Security and Abuse Prevention**
   - The system must enforce usage limits and validation checks to prevent unauthorized or excessive discount application.

## Acceptance Criteria

### Coupon Creation & Management
- [ ] Administrators can create coupon definitions with the following attributes:
  - Code (unique, case-insensitive)
  - Discount type (percentage or fixed-amount)
  - Discount value
  - Currency (for fixed-amount)
  - Validity window (start/end timestamps)
  - Eligibility rules (minimum order subtotal, allowed customer segments, product/category filters)
  - Usage limits (per coupon total, per customer, per order)
- [ ] Coupon creation fails if required fields are missing, values are out of allowed range, or code collides with an existing active coupon.
- [ ] Administrators can activate, deactivate, or modify future-dated coupons without affecting past redemptions.

### Checkout Application
- [ ] Customers can enter a coupon code at checkout.
- [ ] The system validates the coupon and, if valid, applies the calculated discount before payment authorization.
- [ ] Customers receive descriptive error messages for invalid or ineligible coupons (e.g., expired, usage exceeded, minimum subtotal not met).
- [ ] Applied coupon information (code, discount amount) appears in the order summary and confirmation email.
- [ ] Removing a coupon restores the original pricing and frees the reservation of the coupon usage (if applicable).

### Reporting & Audit
- [ ] Coupon redemptions, including order reference, customer ID, timestamp, and discount value, are persisted for auditing.
- [ ] Administrators can retrieve usage metrics aggregated by coupon, including total redemptions, remaining quota, and failure counts.
- [ ] All validation failures are logged with reason codes for monitoring.

## Functional Logic

### Coupon Validation Logic
1. **Code Lookup**
   - Normalize the submitted code (trim, uppercase) and look up an active coupon record.
   - If not found or inactive, return `INVALID_CODE` error.
2. **Temporal Validity**
   - Ensure current time is within the coupon's start and end timestamps; otherwise return `EXPIRED` or `NOT_STARTED`.
3. **Usage Limits**
   - Check total redemption count against the coupon's maximum allowed uses; reject with `MAX_REDEMPTIONS_REACHED` if exceeded.
   - Evaluate customer-specific usage (e.g., per-account, per-email). Reject with `CUSTOMER_USAGE_EXCEEDED` when the limit is reached.
   - If only one coupon per order is allowed, verify no other coupons are applied; otherwise return `COUPON_NOT_COMBINABLE`.
4. **Eligibility Rules**
   - Confirm the order subtotal meets any minimum or maximum thresholds.
   - Validate customer eligibility (segment, loyalty tier, new vs. returning).
   - Enforce product or category constraints: only apply if items in the cart are within the allowed set and not explicitly excluded.
   - Reject with descriptive errors (e.g., `MIN_SUBTOTAL_NOT_MET`, `CUSTOMER_NOT_ELIGIBLE`, `EXCLUDED_ITEMS_PRESENT`).
5. **Reservation (Optional)**
   - Temporarily reserve a usage slot when a coupon is applied but not yet redeemed to avoid race conditions. Release the reservation if the order is abandoned or the coupon is removed before checkout completes.

### Discount Calculation
1. **Percentage Coupons**
   - Discount = `eligible_item_subtotal * (percentage / 100)`.
   - Apply a maximum cap if defined.
   - Round discount according to currency rules (e.g., bankers rounding to two decimals).
2. **Fixed-Amount Coupons**
   - Discount = minimum of `defined_amount` and `eligible_item_subtotal` to avoid negative totals.
   - Support currency conversion if coupon currency differs from order currency.
3. **Application Scope**
   - If the coupon applies only to specific items/categories, calculate discount on eligible items only.
   - Ensure tax and shipping rules comply with business policy (e.g., discount applies before tax unless otherwise specified).
4. **Stacking Rules**
   - If stacking is allowed, apply coupons in priority order and recalculate remaining subtotal between coupons.

### Usage Limit Enforcement
1. **Per Coupon Limit**
   - Store an atomic counter of successful redemptions and compare it against the configured max redemption count.
   - Use database transactions or distributed locks to prevent race conditions.
2. **Per Customer Limit**
   - Track redemptions per customer (by customer ID, email, or loyalty ID). Enforce limits such as one redemption per customer.
3. **Per Order Limit**
   - Ensure only one coupon of the same type or total is applied per order as configured.
4. **Reset & Expiry**
   - Optional ability to reset usage counts after a defined period (e.g., monthly) while preserving historical records.
5. **Audit Trail**
   - Record each redemption with metadata (order ID, timestamp, user) to support fraud investigation and analytics.

### Error Handling & Messaging
- Provide standardized error codes and localized messages for each validation failure to inform the customer and support teams.
- Log validation failures with context for monitoring and alerting.

### Security Considerations
- Rate-limit coupon validation attempts to mitigate brute-force guessing of codes.
- Avoid exposing internal validation rules in error messages beyond what is necessary for customer clarity.
- Ensure coupon codes are treated as case-insensitive tokens to prevent duplication and confusion.

## Non-Functional Requirements
- Validation and discount calculation must complete within the checkout SLA (e.g., <200ms under normal load).
- The system should support concurrent usage spikes during promotions without data integrity issues.
- All coupon data must comply with data retention and privacy policies, especially when tied to customer identifiers.

## Open Questions
- Should coupons be applicable to shipping fees or taxes?
- Are there special coupons (e.g., free shipping, buy-one-get-one) that need additional logic beyond percentage/fixed discounts?
- What is the policy for refunds or returns on orders with applied coupons?
- How will administrators configure and manage coupon campaigns (UI vs. API)?

