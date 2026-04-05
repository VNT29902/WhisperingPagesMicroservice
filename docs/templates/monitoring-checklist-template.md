# Monitoring Checklist Template (Per Service)

## Service Information
- Service name:
- Owner:
- Date:

## A) Health
- [ ] Liveness endpoint configured
- [ ] Readiness endpoint configured
- [ ] Dependency checks included (DB/cache/external APIs)

## B) Metrics
- [ ] Request throughput
- [ ] Error rate
- [ ] Latency p50/p95/p99
- [ ] Resource usage (CPU/memory)
- [ ] DB/connection pool metrics
- [ ] Queue/stream lag metrics (if applicable)

## C) Logs
- [ ] Correlation/trace ID in logs
- [ ] Structured logging format
- [ ] Sensitive fields masked
- [ ] Actionable error context present

## D) Tracing
- [ ] Distributed tracing enabled
- [ ] Critical operations traced

## E) Alerts
- [ ] Availability alert
- [ ] Error rate alert
- [ ] Latency alert
- [ ] Dependency failure alert
- [ ] Queue lag alert (if applicable)

## F) Dashboards
- [ ] Service overview dashboard
- [ ] Endpoint performance dashboard
- [ ] Dependency health dashboard
