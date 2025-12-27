#!/bin/bash
cd "$(dirname "$0")/anunciosloc"
export DATABASE_URL="jdbc:postgresql://ep-gentle-night-abh88026-pooler.eu-west-2.aws.neon.tech:5432/neondb?sslmode=require"
export DATABASE_USERNAME="neondb_owner"
export DATABASE_PASSWORD="npg_uceXHk73TPKp"
bash mvnw spring-boot:run
