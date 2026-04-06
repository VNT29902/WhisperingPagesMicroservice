# Chạy lại toàn bộ hệ thống sau khi pull code (Bash)

Tài liệu này hướng dẫn cách pull code, rebuild Docker image và chạy lại application bằng Bash.

## Cách nhanh nhất

Từ thư mục gốc repo, chạy:

```bash
./scripts/restart-local.sh
```

> Script nằm trong thư mục `scripts/` ở root repo.

Script sẽ tự động:
1. `git pull --rebase`
2. `docker compose down --remove-orphans`
3. `docker compose build --pull`
4. `docker compose up -d`
5. Hiển thị trạng thái container bằng `docker compose ps`

## Nếu đã pull code trước đó

Bạn có thể bỏ qua bước pull:

```bash
./scripts/restart-local.sh --skip-pull
```

Xem trợ giúp:

```bash
./scripts/restart-local.sh --help
```

## Kiểm tra nhanh sau khi chạy

```bash
docker compose ps
```

URL mặc định:
- Frontend: <http://localhost:4200>
- Gateway: <http://localhost:8085>

## Lưu ý

- Cần cài Docker + Docker Compose trước khi chạy.
- Nếu đổi biến môi trường, hãy export hoặc tạo `.env` trước khi chạy script.
