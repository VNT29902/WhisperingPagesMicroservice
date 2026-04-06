#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

usage() {
  cat <<'USAGE'
Usage:
  ./scripts/restart-local.sh [--skip-pull]

Options:
  --skip-pull   Skip `git pull --rebase` step.
USAGE
}

if [[ "${1:-}" == "--help" || "${1:-}" == "-h" ]]; then
  usage
  exit 0
fi

if [[ "${1:-}" != "" && "${1:-}" != "--skip-pull" ]]; then
  echo "[ERROR] Unknown option: ${1}"
  usage
  exit 1
fi

if [[ ! -f "docker-compose.yml" ]]; then
  echo "[ERROR] docker-compose.yml not found. Please run this script from repository root."
  exit 1
fi

if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] docker is not installed."
  exit 1
fi

if docker compose version >/dev/null 2>&1; then
  DCMD=(docker compose)
elif command -v docker-compose >/dev/null 2>&1; then
  DCMD=(docker-compose)
else
  echo "[ERROR] docker compose is not available."
  exit 1
fi

if [[ "${1:-}" != "--skip-pull" ]]; then
  echo "[INFO] Pulling latest code..."
  git pull --rebase
else
  echo "[INFO] Skipping git pull (--skip-pull)."
fi

echo "[INFO] Stopping existing containers..."
"${DCMD[@]}" down --remove-orphans

echo "[INFO] Building latest images..."
"${DCMD[@]}" build --pull

echo "[INFO] Starting services in background..."
"${DCMD[@]}" up -d

echo "[INFO] Current container status:"
"${DCMD[@]}" ps

echo "[SUCCESS] Application has been restarted."
echo "- Frontend: http://localhost:4200"
echo "- Gateway:  http://localhost:8085"
