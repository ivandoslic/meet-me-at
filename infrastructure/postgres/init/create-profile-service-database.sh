#!/usr/bin/env bash
set -euo pipefail

psql \
  --variable=ON_ERROR_STOP=1 \
  --username="$POSTGRES_USER" \
  --dbname="$POSTGRES_DB" \
  --set=profile_user="$PROFILE_DB_USER" \
  --set=profile_password="$PROFILE_DB_PASSWORD" <<'SQL'
CREATE ROLE :"profile_user"
    WITH LOGIN
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE
    PASSWORD :'profile_password';
SQL

psql \
  --variable=ON_ERROR_STOP=1 \
  --username="$POSTGRES_USER" \
  --dbname="$POSTGRES_DB" \
  --set=profile_database="$PROFILE_DB_NAME" \
  --set=profile_user="$PROFILE_DB_USER" <<'SQL'
CREATE DATABASE :"profile_database"
    OWNER :"profile_user";
SQL