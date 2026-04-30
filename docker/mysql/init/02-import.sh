#!/usr/bin/env bash
set -euo pipefail

echo "Importing Dump20210903.sql into database '${MYSQL_DATABASE}'..."
mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < /docker-entrypoint-initdb.d/Dump20210903.sql
echo "Import complete."

