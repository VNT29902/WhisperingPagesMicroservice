# Whispering Pages - Professional Gap Analysis & Action Plan (Revised)

## 1) Mục tiêu tài liệu
Tài liệu này chuyển gap analysis thành kế hoạch triển khai cụ thể cho team Product/Backend/Frontend/QA/DevOps, có ưu tiên, tiêu chí đo lường và đầu việc có thể thực thi ngay.

## 2) Current-state snapshot (dựa trên hệ thống hiện tại)
### Điểm mạnh hiện có
- Đã có luồng thương mại cốt lõi: duyệt sách, tìm kiếm, giỏ hàng, checkout, đơn hàng, tài khoản, dashboard admin.
- Đã tách microservices (Auth, User, Product, Cart, Order, Gateway) và có Docker Compose để chạy local tích hợp.
- Đã có khung báo cáo kinh doanh ở dashboard.

### Điểm yếu cốt lõi
- Chưa đủ tính năng chuyển đổi của website bán sách chuyên nghiệp (wishlist/reviews/recommendation).
- Maturity về fulfillment/return/refund còn mỏng.
- Monitoring/observability chưa đạt mức production-grade.
- Cần chuẩn hóa tài liệu, yêu cầu và checklist phát hành.

---

## 3) Gap matrix (hiện tại vs chuẩn professional)

| Domain | Current | Professional baseline | Gap severity | Owner chính |
|---|---|---|---|---|
| Catalog metadata | Basic fields | ISBN/publisher/language/format/edition/pages | High | Product + BE |
| Discovery & conversion | Category/search/best-selling | Wishlist, reviews, recommendations, recently viewed | High | FE + BE |
| Checkout & payment | Basic payment & order states | Payment resilience, callback/reconcile, richer statuses | High | BE |
| Fulfillment ops | Delivered flow cơ bản | Shipment events, tracking, return/refund | High | BE + Ops |
| Security posture | Cần chuẩn hóa secret handling | Secret manager + rotation + audit | Critical | DevOps + BE |
| Observability | Health-level visibility | Metrics/logs/traces/SLO/alerts | Critical | DevOps |
| QA & release safety | Có test mức cơ bản | CI gate + regression chuẩn + contract tests | High | QA + FE + BE |
| Docs governance | Có tài liệu nền | Living docs + traceability + runbooks | Medium | Tech Lead |

---

## 4) Backlog đề xuất theo mức ưu tiên

## P0 - Critical foundation (2 tuần)
1. **Secrets hardening**
   - Externalize toàn bộ secrets khỏi source config.
   - Rotate credentials và ban hành guideline quản lý secrets.
   - Deliverable: security runbook + kiểm tra không còn secret cứng.
2. **Observability baseline**
   - Chuẩn hóa metrics/log/traces cho toàn bộ services.
   - Dashboard: availability, latency p95, error rate, dependency health.
   - Alert rules: service down, 5xx spike, latency spike.
3. **Release checklist gate**
   - Áp dụng PR checklist bắt buộc cho mọi merge.
   - Áp dụng local UI regression checklist cho thay đổi UI.

## P1 - Conversion uplift (3-4 tuần)
1. Wishlist/save-for-later.
2. Reviews & rating (kèm moderation policy).
3. Recently viewed + related books (rule-based v1).

## P2 - Commerce operations maturity (3-4 tuần)
1. Mở rộng vòng đời đơn hàng (packed/shipped/out-for-delivery/failed-delivery).
2. Return/refund workflow + admin handling.
3. Shipment tracking timeline cho user.

## P3 - Optimization at scale (3-4 tuần)
1. Catalog enrichment + faceted filtering.
2. Search relevance tuning.
3. Performance/load tests + reliability drills.

---

## 5) KPI/Success metrics (đo hiệu quả sau nâng cấp)
- Conversion rate: +10% sau rollout P1.
- Cart abandonment: giảm 10-15%.
- API p95 latency: < 300ms cho core read APIs.
- 5xx rate: < 0.5%/day trên gateway.
- MTTR: giảm 30% nhờ observability.
- Regression defects sau release: giảm 40%.

---

## 6) Definition of Done (DoD) bắt buộc
Một feature chỉ được coi là hoàn thành khi đáp ứng đủ:
1. Có requirement doc với acceptance criteria rõ.
2. Có impact analysis (UI/API/DB/Security/Monitoring).
3. Có test evidence phù hợp (unit/integration/e2e).
4. Có monitoring update (metrics + alerts nếu cần).
5. Chạy local regression checklist (nếu ảnh hưởng UI/flow).
6. Có rollout + rollback notes.

---

## 7) Governance và nhịp vận hành
- Weekly planning (Product + Engineering): ưu tiên backlog và release scope.
- Weekly architecture/ops review: reliability, security, monitoring.
- PR policy: không merge nếu thiếu requirement reference hoặc thiếu checklist evidence.

---

## 8) Artifacts đi kèm trong repo
- Feature Requirement Template
- Monitoring Checklist Template
- Local UI Regression Checklist
- PR Checklist Template
- Service Status Runbook Template

Các templates này là bộ chuẩn tối thiểu để team thực thi roadmap một cách nhất quán.
