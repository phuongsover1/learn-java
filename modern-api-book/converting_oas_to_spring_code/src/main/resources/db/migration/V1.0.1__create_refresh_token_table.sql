create TABLE IF NOT EXISTS ecomm.user_token (
    id uuid NOT NULL DEFAULT random_uuid(),
    refresh_token varchar(128),
    user_id uuid NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES ecomm."user"(id)
    );