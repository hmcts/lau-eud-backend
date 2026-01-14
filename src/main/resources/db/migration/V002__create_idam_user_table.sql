CREATE TABLE idam_user_change_audit (
  id BIGSERIAL PRIMARY KEY,

  user_id VARCHAR(64) NOT NULL,
  principal_user_id VARCHAR(64),

  event_type VARCHAR(64) NOT NULL,
  event_name VARCHAR(64) NOT NULL,

  event_value TEXT,
  previous_event_value TEXT,

  event_timestamp TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_idam_user_change_audit_user_id
  ON idam_user_change_audit (user_id);

CREATE INDEX idx_idam_user_change_audit_principal_user_id
  ON idam_user_change_audit (principal_user_id);

