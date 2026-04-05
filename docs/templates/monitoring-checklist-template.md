# Monitoring Checklist Template (Production-ready)

## Service info
- Service:
- Owner:
- Environment: local / staging / production
- Date:

## 1) Health & Availability
- [ ] Liveness endpoint
- [ ] Readiness endpoint
- [ ] Dependency health (DB, Redis, external API)
- [ ] Startup/shutdown health behavior reviewed

## 2) Metrics
- [ ] Request rate (RPS)
- [ ] Error rate (4xx/5xx)
- [ ] Latency p50/p95/p99
- [ ] CPU/memory usage
- [ ] DB pool saturation
- [ ] Queue lag / stream lag (if applicable)

## 3) Logs
- [ ] Structured logs
- [ ] Correlation ID / trace ID
- [ ] Sensitive data masked
- [ ] Error logs include actionable context

## 4) Tracing
- [ ] Distributed tracing enabled
- [ ] Upstream/downstream spans validated
- [ ] Slow-path traces sampled and reviewable

## 5) Alerts
- [ ] Service down
- [ ] Error-rate spike
- [ ] Latency threshold breach
- [ ] Dependency failure
- [ ] Queue/stream lag threshold breach

## 6) Dashboards
- [ ] Service overview
- [ ] Endpoint performance
- [ ] Dependency health
- [ ] Business KPI linkage (if applicable)

## 7) Operational Readiness
- [ ] Runbook exists
- [ ] On-call knows alert ownership
- [ ] Incident escalation path documented
