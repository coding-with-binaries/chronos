CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO roles(
    uid,
    created_by,
    last_modified_by,
    type
)
SELECT uuid_generate_v4(), 'system', 'system', 0
WHERE
NOT EXISTS (
    SELECT type FROM roles WHERE type = 0
);

INSERT INTO
users(
    uid,
    created_by,
    last_modified_by,
    enabled,
    deactivated,
    username,
    password,
    role_id
)
SELECT
    uuid_generate_v4(),
    'system',
    'system',
    true,
    false,
    'administrator',
    '$2a$10$hPaVNsvFQNcxWGRKRsmKF.TS5mULhBb3u/KlonKcl4jf4LVAajRvy',
    (SELECT uid FROM roles where type=0)
WHERE
NOT EXISTS (
    SELECT username FROM users WHERE username = 'administrator'
);