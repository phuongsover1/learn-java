ALTER TABLE ecomm."user"
    ADD role varchar(16) NOT NULL DEFAULT 'ROLE_USER' NULL_TO_DEFAULT;

ALTER TABLE ecomm."user"
    ALTER COLUMN password varchar(72);


UPDATE ecomm."user"
SET password = '{bcrypt}$2a$10$neR0EcYY5./tLVp4litNyuBy/kfrTsqEv8hiyqEKX0TXIQQwC/5Rm',
    role = 'ROLE_USER'
WHERE username = 'scott';

UPDATE ecomm."user"
SET password = '{bcrypt}$2a$10$neR0EcYY5./tLVp4litNyuBy/kfrTsqEv8hiyqEKX0TXIQQwC/5Rm',
    role = 'ROLE_ADMIN'
WHERE username = 'scott2';
