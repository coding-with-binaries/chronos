CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO roles(
    uid,
    created_by,
    created_on,
    last_modified_by,
    last_modified_on,
    type
)
SELECT uuid_generate_v4(), 'system',(extract(epoch from now()) * 1000), 'system',(extract(epoch from now()) * 1000), 0
WHERE
NOT EXISTS (
    SELECT type FROM roles WHERE type = 0
);

INSERT INTO
users(
    uid,
    created_by,
    created_on,
    last_modified_by,
    last_modified_on,
    enabled,
    deactivated,
    username,
    password,
    role_id
)
SELECT
    uuid_generate_v4(),
    'system',
    (extract(epoch from now()) * 1000),
    'system',
    (extract(epoch from now()) * 1000),
    true,
    false,
    'administrator',
    '$2a$10$hPaVNsvFQNcxWGRKRsmKF.TS5mULhBb3u/KlonKcl4jf4LVAajRvy',
    (SELECT uid FROM roles where type=0)
WHERE
NOT EXISTS (
    SELECT username FROM users WHERE username = 'administrator'
);