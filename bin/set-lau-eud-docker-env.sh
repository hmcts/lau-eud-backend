#!/bin/sh

export SERVER_PORT=4553

# Database
export LAU_EUD_DB_NAME=lau_eud
export LAU_EUD_DB_HOST=0.0.0.0
export LAU_EUD_DB_PORT=5054
export LAU_EUD_DB_USERNAME=lauuser
export LAU_EUD_DB_PASSWORD=laupass
export LAU_EUD_DB_ADMIN_USERNAME=lauadmin
export LAU_EUD_DB_ADMIN_PASSWORD=laupass
export LAU_EUD_ENCRYPTION_KEY=my_very_secure_key

export FLYWAY_PLACEHOLDERS_LAU_EUD_DB_USERNAME=lauuser
export FLYWAY_PLACEHOLDERS_LAU_EUD_DB_PASSWORD=laupass
export FLYWAY_URL=jdbc:postgresql://0.0.0.0:5054/lau_eud
export FLYWAY_USER=lauadmin
export FLYWAY_PASSWORD=laupass
export FLYWAY_NOOP_STRATEGY=false
