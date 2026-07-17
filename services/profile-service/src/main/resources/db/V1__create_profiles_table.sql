CREATE TABLE profiles
(
    user_id      UUID PRIMARY KEY,
    display_name VARCHAR(80)  NOT NULL,
    bio          VARCHAR(500),
    avatar_url   VARCHAR(2048),
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT profiles_display_name_not_blank
        CHECK (BTRIM(display_name) <> '')
);