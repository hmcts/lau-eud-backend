GRANT USAGE, SELECT ON SEQUENCE idam_user_change_audit_id_seq TO lauuser;
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE idam_user_change_audit, flyway_schema_history TO lauuser;
