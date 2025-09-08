-- create user for application access
  DO
  $do$
  BEGIN
     IF NOT EXISTS (
        SELECT FROM pg_catalog.pg_roles  -- SELECT list can be empty for this
        WHERE  rolname = 'lauuser') THEN
        CREATE ROLE lauuser LOGIN PASSWORD '${LAU_EUD_DB_PASSWORD}';
     END IF;
  END
  $do$;

 CREATE EXTENSION IF NOT EXISTS pgcrypto;
