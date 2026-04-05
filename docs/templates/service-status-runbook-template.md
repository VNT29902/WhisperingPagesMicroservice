# Service Status Runbook Template

## Purpose
Hướng dẫn kiểm tra nhanh trạng thái hệ thống sau khi deploy/chỉnh sửa tính năng.

## 1) Container status
```bash
docker compose ps -a
```

## 2) Check service logs
```bash
docker compose logs --tail=200 <service-name>
```

## 3) Health endpoint checks
```bash
curl -s http://localhost:<port>/actuator/health
```

## 4) Gateway reachability
```bash
curl -I http://localhost:<gateway-port>
```

## 5) Dependency checks
- Database reachable
- Redis reachable
- Service discovery reachable

## 6) Incident triage checklist
- [ ] Xác định service lỗi đầu tiên
- [ ] Thu thập log + thời điểm lỗi
- [ ] Xác định dependency bị ảnh hưởng
- [ ] Tạm thời rollback/restart nếu cần
- [ ] Tạo incident note và action items
