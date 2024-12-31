create table if not exists ecomm.user_token (
    id UUID NOT NULL DEFAULT random_uuid(),
    refresh_token varchar(128) NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES ecomm."user"(id)
);